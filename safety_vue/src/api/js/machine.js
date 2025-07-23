function drawMachine() {
  let ctx = main_ctx;
  ctx.globalCompositeOperation = "source-over";
  let fpx = 2 / canvasOpt.factor / 2; // 字体大小设置
  ctx.font = fpx + 'px sans-serif';
  ctx.textAlign = 'center';
  ctx.fillStyle = 'black';

  showList.forEach(function (rollerId) {
    if (allWorkData[rollerId] == undefined) {
      return;
    }
    let index = calcWorkIndex(allInfoData[rollerId]);
    var len = allWorkData[rollerId].length;
    let nextIndex = index + 1;
    if (index == len - 1) {
      // 最后一个点时，机械暂停
      nextIndex = index;
    } else if (allInfoData[rollerId][index].gpsTime + 1000 < allInfoData[rollerId][index + 1].gpsTime) {
      // 点的时间不连续时，机械暂停
      nextIndex = index;
    }
    if (!isWork(allInfoData[rollerId][0].gpsTime)) {
      // 机械还没开始工作时，机械暂停
      nextIndex = index;
    }
    if (!isPlay) {
      // 暂停
      nextIndex = index;
    }


    var curPt = allWorkData[rollerId][index];
    var nextPt = allWorkData[rollerId][nextIndex];
    var curAng = allAngData[rollerId][index];
    var nextAng = allAngData[rollerId][nextIndex];

    // 机械运动平滑处理
    var curTime = new Date().getTime();
    let cur = {
      x: curPt.x,
      y: curPt.y,
      r: curAng,
    };
    let next = {
      x: nextPt.x,
      y: nextPt.y,
      r: nextAng,
    };
    let gap = {
      x: next.x - cur.x,
      y: next.y - cur.y,
      r: next.r - cur.r,
    };

    let increment = (curTime - indexTime) / 1000;
    let curPos = {
      x: cur.x + increment * gap.x,
      y: cur.y + increment * gap.y,
      r: cur.r + increment * gap.r,
    }

    drawOne(allInfoData[rollerId][index], curPos, rollerId);
  })


  function drawOne(info, curPos, rollerId) {
    // 以机械为中心
    if (focus_machine == rollerId) {
      moveToCenter(main_ctx, curPos.x, curPos.y);
    }

    // 图片信息 266 * 141
    let size = machine_info[rollerId].machine_size;
    let mh = size / canvasOpt.factor * 1.1;
    let mw = size * 243 / 111 / canvasOpt.factor * 1.1;

    // 机械角度
    ctx.save();
    ctx.translate(curPos.x, curPos.y);
    ctx.rotate(curPos.r);
    let px = 0 - mw / 2;
    let py = 0 - mh / 2;

    ctx.drawImage(machineImg, px, py, mw, mh);
    ctx.restore();


    // 机械信息文字 和 速度
    let name = machine_info[rollerId].machine_name;
    let v = info.velocity;  // m/s
    v =  (v * 3.6).toFixed(2);
    v = `速度：${v}km/h`
    ctx.fillText(name, curPos.x, curPos.y - mw*3/2);
    ctx.fillText(v, curPos.x, curPos.y - mw*3/2 + fpx);
  }

}


/**
 * 判断机器是否开始工作
 * @param beginWorkTime
 */
function isWork(beginWorkTime) {
  let start = Date.parse(start_date);
  let nowork = (beginWorkTime - start)/1000;
  return curIndex >= nowork;
}

