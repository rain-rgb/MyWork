/**
 * canvas的一些基本操作，依赖mlMatrix库
 */


/**
 * 清除画布内容
 * @param ctx Canvas 2D渲染上下文
 */
function clearCanvas(ctx) {
  let canvas = ctx.canvas;
  let width = canvas.width;
  let height = canvas.height;
  ctx.save()
  ctx.setTransform(1, 0, 0, 1, 0, 0);
  ctx.clearRect(0, 0, width, height);
  ctx.restore();
}

/**
 * canvas坐标转client坐标 矩阵计算比较费时，大量调用时请使用canvas2client_op
 * canvas坐标点-> 变换矩阵 -> client坐标点
 * @param ctx
 * @param x
 * @param y
 * @returns {{x, y}}
 */
function canvas2client(ctx, x, y) {
  let dmt = ctx.getTransform();
  let matrix = new mlMatrix.Matrix(domMatrix2Arr(dmt));
  let origin = new mlMatrix.Matrix([[x], [y], [1]]);
  let pt = matrix.mmul(origin);
  return {x: pt.get(0, 0), y: pt.get(1, 0)};
}

/**
 * canvas坐标转client坐标优化方案，使用全局变量计算
 * @param x
 * @param y
 */
function canvas2client_op(x, y) {
  x = x * canvasOpt.scale;
  y = y * canvasOpt.scale;
  return {
    x: canvasOpt.originScreen.x + x,
    y: canvasOpt.originScreen.y + y,
  }
}

/**
 * client坐标转canvas坐标
 * client坐标点-> 变换矩阵逆矩阵 -> canvas坐标点
 * @param ctx
 * @param x
 * @param y
 */
function client2canvas(ctx, x, y) {
  let dmt = ctx.getTransform();
  let matrix = new mlMatrix.Matrix(domMatrix2Arr(dmt));
  let inverse = mlMatrix.inverse(matrix);
  let clientM = new mlMatrix.Matrix([[x], [y], [1]]);
  let pt = inverse.mmul(clientM);
  return {x: pt.get(0, 0), y: pt.get(1, 0)};
}

function client2canvas_op(x, y) {
  let dx = x - canvasOpt.originScreen.x;
  let dy = y - canvasOpt.originScreen.y;
  return {
    x: dx / canvasOpt.scale,
    y: dy / canvasOpt.scale,
  }
}

/**
 * 将DOMMatrix转换为数组表示的矩阵，方便后续计算
 * @param{DOMMatrix} domMatrix:通过getTransform得到的当前canvas的变换矩阵
 * @returns {((*|string)[]|number[])[]}
 */
function domMatrix2Arr(domMatrix) {
  return [
    [domMatrix.a, domMatrix.c, domMatrix.e],
    [domMatrix.b, domMatrix.d, domMatrix.f],
    [0, 0, 1]
  ]
}

/**
 * 将指定的点(canvas坐标点)移动到画布中央位置
 * @param ctx
 * @param x
 * @param y
 */
function moveToCenter(ctx, x, y) {
  let canvas = ctx.canvas;
  let width = canvas.width;
  let height = canvas.height;
  let centerPt = client2canvas_op(width / 2, height / 2);
  ctxList.forEach(function(ctx) {
    ctx.translate(centerPt.x - x, centerPt.y - y);
  })
  canvasOpt.originScreen = canvas2client(ctx, 0, 0);
}

/**
 * 计算2向量之间的夹角
 * @param x1
 * @param y1
 * @param x2
 * @param y1
 */
function getAngle(x1, y1, x2, y2) {
  let dist1 = getDist(x1, y1, 0, 0);
  let dist2 = getDist(x2, y2, 0, 0);
  let cosA = (x1 * x2 + y1 * y2) / (dist1 * dist2);
  return cosA;
}

/**
 * 2点之间的距离
 * @param x1
 * @param y1
 * @param x2
 * @param y2
 */
function getDist(x1, y1, x2, y2) {
  let dist = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  return dist;
}

/**
 * 判断2点之间的距离是否在指定的区域内
 * @param x1
 * @param y1
 * @param x2
 * @param y2
 * @param minDis
 * @param maxDis
 * @param scale
 * @returns {boolean}
 */
function normalDis(x1, y1, x2, y2, minDis, maxDis, scale) {
  let min = minDis / scale;
  let max = maxDis / scale;
  let dist = getDist(x1, y1, x2, y2);
  return dist >= min && dist <= max;
}

/**
 * 角度转弧度
 * @param angle
 * @returns {number}
 */
function angle2radian(angle) {
  return angle * 2 * Math.PI / 360;
}

/**
 * 弧度转角度
 * @param radian
 * @returns {number}
 */
function radian2angle(radian) {
  return radian / (2 * Math.PI) * 360;
}

/**
 * 判断点是否在canvas视窗内
 * @param ctx
 * @param x
 * @param y
 * @returns {boolean}
 */
function isInScreen(x, y) {
  let width = canvasOpt.width;
  let height = canvasOpt.height;
  let screenPt = canvas2client_op(x, y);
  return (screenPt.x >= -50 && screenPt.x <= width + 50 && screenPt.y >= -50 && screenPt.y <= height + 50);
}

/**
 * 北东坐标转换为canvas坐标
 */
function nePt2canvasPt(pts, boundary, factor) {
  let res = [];
  pts.forEach(function (pt) {
    let x = (pt.east - boundary.left) / factor;
    let y = (boundary.top - pt.north) / factor;
    res.push({x: x, y: y});
  })
  return res;
}

/**
 * 根据误差范围errMin和errMax，分割点数组，使结果数组中的每一个子数组内的点距离都在正常
 * 范围内
 * @param pts
 * @param errMin 规定的两点最小距离
 * @param errMax 规定的两点最大距离
 * @returns {[]}
 */
function splitPts(pts, errMin, errMax, factor) {
  // 分割点数组，分割后的子数组内，点之间的距离都在正常范围内
  let ptsArr = [], subPts = [];
  for (let i = 0; i < pts.length - 1; i++) {
    let p1 = pts[i], p2 = pts[i + 1];
    subPts.push(p1);
    if (!normalDis(p1.x, p1.y, p2.x, p2.y, errMin, errMax, factor)) {
      ptsArr.push(subPts);
      subPts = [];
    }
  }

  // 最后一个点
  subPts.push(pts[pts.length - 1]);
  ptsArr.push(subPts);

  return ptsArr;
}

/**
 * 单运动轨迹点，转换为双轨迹点
 * @param pts
 * @param pathWidth 轮宽
 * @param factor 工程点范围和canvas视口的比例
 * @returns {[]}
 */
function getTrackPts(pts, pathWidth, factor) {
  let ptsArr = splitPts(pts, 0.1, 10, factor);
  let firstPt = pts[0];

  let trackPts = [];
  ptsArr.forEach(function (subpts) {
    let last = trackPts.slice(-2);
    let temp = getPts(subpts);
    if (temp.length === 0) {
      if (last.length == 0) {
        // 第一个点就超出范围的处理
        let w = pathWidth / factor / 2;
        temp = [{x: firstPt.x - w, y: firstPt.y}, {x: firstPt.x + w, y:firstPt.y}];
      } else {
        temp = last;
      }
    }
    trackPts = trackPts.concat(temp);
  })
  return trackPts;

  // 经过剔除后，没有太近或者太远的点
  function getPts(subPts) {
    let res = [];
    for (let i = 0; i < subPts.length - 1; i++) {
      let width = pathWidth / factor / 2;
      let p1 = subPts[i], p2 = subPts[i + 1];
      let theta = Math.atan2(p2.y - p1.y, p2.x - p1.x);
      let dx = width * Math.sin(theta);
      let dy = width * Math.cos(theta);
      let pt1 = {}, pt2 = {};
      pt1.x = p1.x - dx;
      pt1.y = p1.y + dy;
      pt2.x = p1.x + dx;
      pt2.y = p1.y - dy;
      res.push(Object.assign({}, pt1), Object.assign({}, pt2));
      // 处理最后一个点
      if (i == subPts.length - 2) {
        pt1.x = p2.x - dx;
        pt1.y = p2.y + dy;
        pt2.x = p2.x + dx;
        pt2.y = p2.y - dy;
        res.push(pt1, pt2);
      }
    }
    return res;
  }
}

/**
 * 计算每个点的机械方向
 * @param pts
 * @returns {[]}
 */
function getPtsAngle(pts, factor) {
  let ptsArr = splitPts(pts, 0.1, 10, factor);

  let angArr = [];
  ptsArr.forEach(function(pts) {
    let sub = getAng(pts);
    if (sub.length == 0) {
      if (angArr.length === 0) {
        sub = [0];
      } else {
        sub = angArr.slice(-1);
      }
    }
    angArr = angArr.concat(sub);
  })
  return angArr;

  // 计算每个点的方向
  function getAng(subPts) {
    let res = [];
    let last = 0;
    for (let i = 0; i < subPts.length - 1; i++) {
      let p1 = subPts[i], p2 = subPts[i+1];
      let v = {x: p2.x - p1.x, y: p2.y - p1.y};
      let ang = Math.atan2(v.y, v.x);
      res.push(ang);
      last = ang;
      // 处理最后一个点
      if (i == subPts.length - 2) {
        res.push(last);
      }
    }
    return res;
  }
}

/**
 * 获取所有点的边界信息（左上右下）
 * @param pts 点（北东坐标）数组
 */
function getBoundary(pts) {
  let res = {
    left: Number.MAX_VALUE,
    top: Number.MIN_VALUE,
    right: Number.MIN_VALUE,
    bottom: Number.MAX_VALUE,
  }
  pts.forEach(function (pt) {
    res.left = Math.min(res.left, pt.east);
    res.top = Math.max(res.top, pt.north);
    res.right = Math.max(res.right, pt.east);
    res.bottom = Math.min(res.bottom, pt.north);
  })
  return res;
}


function drawGrid(ctx) {
  let w = ctx.canvas.width;
  let h = ctx.canvas.height;
  ctx.save()
  ctx.lineWidth = 0.1;
  // 横线
  for (let y = 0; y <= h; y += 60) {
    ctx.beginPath();
    ctx.moveTo(0, y);
    ctx.lineTo(w, y);
    ctx.stroke();
  }
  // 竖线
  for (let x = 0; x <= w; x += 60) {
    ctx.beginPath();
    ctx.moveTo(x, 0);
    ctx.lineTo(x, h);
    ctx.stroke();
  }
  ctx.restore();
}

/**
 * 绘制点数组连成的线
 * @param ctx
 * @param pts 点数组，可以处理2种形式的点数组 1：[x0,y0,x1,y1,...] 2: [{x,y}, {x,y},...]
 */
function drawLine(ctx, pts) {
  if (pts.length === 0) return;

  ctx.save();
  ctx.lineWidth = 1 / canvasOpt.scale;
  ctx.beginPath();
  if (pts[0].x !== undefined) {
    pts.forEach(function (pt, index) {
      if (index === 0) {
        ctx.moveTo(pt.x, pt.y);
      } else {
        ctx.lineTo(pt.x, pt.y);
      }
    })
  } else {
    for (let i = 0; i < pts.length - 3; i += 2) {
      ctx.moveTo(pts[i], pts[i + 1]);
      ctx.lineTo(pts[i + 2], pts[i + 3]);
    }
  }
  ctx.stroke();
  ctx.restore();
}


/**********************************************************************************/


function drawBlock(ctx, pts) {
  for (let i = 0; i < pts.length - 3; i += 2) {
    let p1 = pts[i];
    let p2 = pts[i + 1];
    let p3 = pts[i + 2];
    let p4 = pts[i + 3];
    if (!isInScreen(p1.x, p1.y) || !isInScreen(p2.x, p2.y)
      || !isInScreen(p3.x, p3.y) || !isInScreen(p4.x, p4.y)
      || !normalDis((p1.x + p2.x)/2, (p1.y+p2.y)/2, (p3.x+p4.x)/2, (p3.y+p4.y)/2, 0.1, 10, canvasOpt.factor)) {
      continue;
    }
    ctx.beginPath();
    ctx.moveTo(p1.x, p1.y);
    ctx.lineTo(p2.x, p2.y);
    if (sameDirection(p1, p2, p3, p4)) {
      ctx.lineTo(p4.x, p4.y);
      ctx.lineTo(p3.x, p3.y);
    } else {
      ctx.lineTo(p3.x, p3.y);
      ctx.lineTo(p4.x, p4.y);
    }
    ctx.closePath();
    ctx.fill();
  }
}

function drawDot(ctx, pts) {
  let color = ['RGB(0,0,0)', 'RGB(255,0,0)', 'RGB(0,255,0)', 'RGB(0,0,255)', 'RGB(130,130,130)']
  ctx.save();
  for (let i = 0; i < pts.length; i++) {
    let p1 = pts[i], p2 = pts[i + 1];
    // let len = getDist(p1.x, p1.y, p2.x, p2.y);
    // len /= 10;
    let len = 10 / ctx.getTransform().a;
    ctx.beginPath();
    ctx.fillStyle = color[i % 5];
    ctx.fillRect(p1.x, p1.y, len, len);
  }
  ctx.restore();
}

// 点乘
function dotProduct(v1, v2) {
  return v1.x * v2.x + v1.y * v2.y;
}

// 判断p1p2和p3p4方向是否一致
function sameDirection(p1, p2, p3, p4) {
  return dotProduct(vector(p1, p2), vector(p3, p4)) > 0;
}

// 由两点得到向量
function vector(p1, p2) {
  return {
    x: p2.x - p1.x,
    y: p2.y - p1.y
  }
}

// requestAnimationFrame兼容方案
window.requestAnimFrame = (function() {
  return window.requestAnimationFrame || window.webkitRequestAnimationFrame || window.mozRequestAnimationFrame ||
    window.oRequestAnimationFrame || window.msRequestAnimationFrame || function (callback) {
      window.setTimeout(callback, 1000 / 60);
    };
})();
