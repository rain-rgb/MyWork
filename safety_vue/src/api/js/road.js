// 绘制道路和道路中线
function drawRoad() {
  let ctx = main_ctx;
  ctx.globalCompositeOperation = "source-over";
  ctx.lineWidth = 1 / canvasOpt.scale;

  // 绘制道路边界
  ctx.strokeStyle = "red";
  drawLine(ctx, roadData);

  // 绘制道路中线
  ctx.strokeStyle = "rgb(241,148,138)";
  drawLine(ctx, pileNumData);

  drawStation();
}

/**
 * 绘制桩号
 */
function drawStation() {
  let pileLen = pile_num_list.length;
  if (pileLen === 0) {
    return;
  }
  let ctx = main_ctx;
  let gap = getGap();
  let fpx = 20 / canvasOpt.scale; // 字体设置20px

  ctx.save();

  ctx.textAlign = 'center';
  ctx.fillStyle = 'black';
  ctx.font = fpx + 'px sans-serif';

  for (let i = 0; i < pileNumData.length; i += gap) {
    ctx.fillText(formatStation(stationPrefix, pile_num_list[i].pile_num), pileNumData[i].x, pileNumData[i].y);
  }

  // 绘制结束桩号
  ctx.fillText(formatStation(stationPrefix, pile_num_list[pileLen-1].pile_num), pileNumData[pileLen-1].x, pileNumData[pileLen-1].y);

  ctx.restore();

  function getGap() {
    // 视口最少显示3个桩号来计算绘制的桩号点间隔
    let dist = canvasOpt.width / 3 * canvasOpt.factor / canvasOpt.scale;  // 视口高的1/3转换为实际的距离（m）
    let gapNum = parseInt(dist / 5);  // 2个桩号点间隔5m， 计算dist的距离有多少个桩号点
    return gapNum;
  }
}

/**
 * 指定桩号居中
 * @param stationNum
 */
function stationToCenter(stationNum) {
  let pn = parseInt(stationNum / 10) * 10 + 5;

  let index = 0;
  for (let i = 0; i < pile_num_list.length - 1; i++) {
    let pile = pile_num_list[i];
    if (pn >= pile_num_list[i].pile_num && pn <= pile_num_list[i + 1].pile_num) {
      index = i;
      break;
    }
  }

  let pt = pileNumData[index];
  moveToCenter(main_ctx, pt.x, pt.y);
}