init();


// 开始函数
function start() {
  // 初始化canvas
  initCanvas();

  // 绘制背景网格，只需要绘制一次
  drawGrid(grid_ctx);

  // 初始绘制默认放大倍数
  let defaultScale = 4 * canvasOpt.factor;
  let w = canvasOpt.height / 20;
  w = w * canvasOpt.factor / canvasOpt.scale;
  defaultScale = w / 2;
  canvasOpt.scale = defaultScale;
  ctxList.forEach(function (ctx) {
    ctx.scale(defaultScale, defaultScale);
  })
}

/**
 * 初始化变量
 * @param obj 调用传入的对象
 */
function initVariable(obj) {
  section_id = obj.section_id;
  start_date = obj.begin_timeStr;
  end_date = obj.end_timeStr;
  start_station = obj.begin_station;
  end_station = obj.end_station;

  initSlider(new Date(start_date).getTime(), new Date(end_date).getTime());
  initControl();

  // 获取道路和桩号信息
  ajax({
    url: `stake/${section_id}`,
    // async: true,
    success: function (res) {
      // 解析道路边界数据
      let ptsArr = res.obj.boundary.split(',');
      ptsArr.forEach(function (pt) {
        let arr = pt.split(' ');
        road_boundary.push({north: arr[0], east: arr[1]});
      })
      // 封闭道路
      if (ptsArr[0] != ptsArr[ptsArr.length - 1]) {
        road_boundary.push(road_boundary.slice(0, 1)[0]);
      }

      // 道路中线
      pile_num_list = res.obj.stake;

      // 初始化道路信息
      initRoadData(road_boundary, pile_num_list);
    }
  });

  // 获取压实遍数颜色信息
  ajax({
    url: `standard/${section_id}/1`,
    success: function (res) {
      let colorArr = res.obj;
      var len = colorArr.length;
      if (len === 0) {
        return;
      }
      colorArr.sort(function (a, b) {
        return b.number - a.number;
      })
      setTimesColorBox(colorArr);

      timesColor = {
        minTimes: colorArr[len - 1].number,
        maxTimes: colorArr[0].number,
        color: [],
      }
      colorArr.forEach(function (item) {
        timesColor.color.unshift(hexToRGB(item.color));
      })


    }
  });

  // 获取压实轨迹信息
  curStartTime = start_date;
  curEndTime = new Date(curStartTime);
  curEndTime.setTime(curEndTime.getTime() + 60 * 60 * 1000); // 第一次加载一小时的数据
  curEndTime = curEndTime.format('yyyy-MM-dd hh:mm:ss');
  let layerIndex = null;
  ajax({
    url: `data/${section_id}/${curStartTime}/${curEndTime}`,
    async: true,
    beforeSend: function () {
      layerIndex = layer.load(1);
    },
    complete: function () {
      layer.close(layerIndex);
      upDateLoadProgress();
    },
    success: function (res) {

      machine_info = res.obj.device;
      allInfoData = res.obj.gps;
      if ($.isEmptyObject(machine_info) || $.isEmptyObject(allInfoData)) {
        loadData();
      } else {
        loadMachineTable(machine_info);

        // 初始化压实数据
        initCompactData(allInfoData);

        loadSequenceData();
      }

      setPlaySpeed(1);
      render();
    },
  })

  start();
}


/**
 * 加载数据，直到有机械信息为止
 */
function loadData() {
  if (curEndTime == end_date) {
    return;
  }
  curStartTime = curEndTime;
  curEndTime = new Date(curStartTime);
  curEndTime.setTime(curEndTime.getTime() + 60 * 60 * 1000); // 加载一小时的数据
  if (curEndTime.getTime() > Date.parse(end_date)) {
    curEndTime = end_date;
  } else {
    curEndTime = curEndTime.format('yyyy-MM-dd hh:mm:ss');
  }

  ajax({
    url: `data/${section_id}/${curStartTime}/${curEndTime}`,
    async: true,
    complete: function () {
      upDateLoadProgress();
    },
    success: function (res) {
      machine_info = res.obj.device;
      allInfoData = res.obj.gps;
      if ($.isEmptyObject(machine_info) || $.isEmptyObject(allInfoData)) {
        loadData();
      } else {
        loadMachineTable(machine_info);

        // 初始化压实数据
        initCompactData(allInfoData);

        loadSequenceData();
      }
    },
  })
}

/**
 * 加载后续数据
 */
function loadSequenceData() {
  if (curEndTime == end_date) {
    closeCalTrackWorker();
    return;
  }
  curStartTime = curEndTime;
  curEndTime = new Date(curStartTime);
  curEndTime.setTime(curEndTime.getTime() + 60 * 60 * 1000); // 加载一小时的数据
  if (curEndTime.getTime() > Date.parse(end_date)) {
    curEndTime = end_date;
  } else {
    curEndTime = curEndTime.format('yyyy-MM-dd hh:mm:ss');
  }

  ajax({
    url: `data/${section_id}/${curStartTime}/${curEndTime}`,
    async: true,
    complete: function () {
      loadSequenceData();
      upDateLoadProgress();
    },
    success: function (res) {
      var res = res.obj.gps;
      if ($.isEmptyObject(res)) {
        return;
      }

      for (let key in res) {
        var tempData = res[key];
        if (tempData.length == 0) {
          continue;
        }
        let machine = machine_info[key];
        if (!machine) continue;
        let pts = nePt2canvasPt(tempData, boundary, canvasOpt.factor);
        if (allWorkData[key] != undefined) {
          allWorkData[key] = allWorkData[key].concat(pts);
          allInfoData[key] = allInfoData[key].concat(tempData);
        } else {
          allWorkData[key] = pts;
          allInfoData[key] = tempData;
        }
        allAngData[key] = getPtsAngle(allWorkData[key], canvasOpt.factor);
        calTrackPtsByWorker(pts, key);
      }
    },
  })
}