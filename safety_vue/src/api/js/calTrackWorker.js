addEventListener('message', function(e) {
  if (e.data.isEnd !== undefined && e.data.isEnd) {
    self.close();
  }

  let pts = e.data.pts;
  let size = e.data.size;
  let factor = e.data.factor;
  let rollerId = e.data.rollerId;

  let result = getTrackPts(pts, size, factor);
  postMessage({
    rollerId,
    pts: result,
  })
})




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


function normalDis(x1, y1, x2, y2, minDis, maxDis, scale) {
  let min = minDis / scale;
  let max = maxDis / scale;
  let dist = getDist(x1, y1, x2, y2);
  return dist >= min && dist <= max;
}

function getDist(x1, y1, x2, y2) {
  let dist = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
  return dist;
}