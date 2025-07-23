function compackWord() {
  let ctx = buffer_ctx;

  // 设置绘图透明度叠加，后续根据叠加值计算压实遍数
  ctx.globalCompositeOperation = "lighter";
  ctx.fillStyle = "rgba(0, 0, 0, 0.01)";

  for (let rollerId in allTrackData) {
    drawCompackBlock(ctx, allTrackData[rollerId]);
  }

  fillColor();
}

// 绘制压实
function drawCompackBlock(ctx, pts) {
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

// 根据压实遍数填充颜色
function fillColor() {
  let imgData = buffer_ctx.getImageData(0,0, canvasOpt.width, canvasOpt.height);
  let data = imgData.data;
  for (let i = 0; i < data.length; i += 4) {
    if (data[i + 3] === 0) {
      continue;
    }

    // 根据透明度计算压实遍数
    let times = Math.floor(data[i + 3] / 3);
    if (times < timesColor.minTimes) {
      times = timesColor.minTimes;
    } else if (times > timesColor.maxTimes) {
      times = timesColor.maxTimes;
    }

    // 填充颜色
    let rgba = timesColor.color[times - timesColor.minTimes];
    data[i] = rgba[0];
    data[i + 1] = rgba[1];
    data[i + 2] = rgba[2];
    data[i + 3] = 255;
  }

  main_ctx.putImageData(imgData, 0, 0);
}


/**
 * 创建 Worker线程,计算转换轨迹点
 * @param pts
 * @param rollerId
 */
function calTrackPtsByWorker(pts, rollerId) {
  let machine = machine_info[rollerId];

  if (typeof Worker !== "undefined") {
    if (calTrackWorker === null) {
      calTrackWorker = new Worker('js/calTrackWorker.js');
    }

    calTrackWorker.postMessage({
      rollerId: rollerId,
      pts: pts,
      size: machine.machine_size,
      factor: canvasOpt.factor,
    });
    calTrackWorker.onmessage = function(e) {
      allTrackData[e.data.rollerId] = e.data.pts;
    }
  } else {
    allTrackData[rollerId] = getTrackPts(pts, machine.size, canvasOpt.factor);
  }
}

/**
 * 关闭线程
 */
function closeCalTrackWorker() {
  if (calTrackWorker !== undefined) {
    calTrackWorker.postMessage({
      'isEnd': true
    });
  }
}