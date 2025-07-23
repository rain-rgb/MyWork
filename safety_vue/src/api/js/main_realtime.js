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
  start_date = '2020-03-10 08:00:00';
  end_date = new Date(start_date).format('yyyy-MM-dd') + new Date(new Date() - 30000).format(' hh:mm:ss'); // 延迟30s
  start_station = obj.begin_station;
  end_station = obj.end_station;

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
  curEndTime = end_date;
  let layerIndex = null;
  ajax({
    url: `data/${section_id}/${start_date}/${end_date}`,
    async: true,
    beforeSend: function () {
      layerIndex = layer.load(1);
    },
    complete: function () {
      layer.close(layerIndex);
    },
    success: function (res) {
      machine_info = res.obj.device;
      allInfoData = res.obj.gps;

      loadMachineTable(machine_info);

      // 初始化压实数据
      initCompactData(allInfoData);

      // 设置index
      curIndex = (Date.parse(end_date) - Date.parse(start_date) - 60 * 1000) / 1000;

      setPlaySpeed(1);

      realTimeLoad();

      render();
    },
  })

  start();
}


function realTimeLoad() {
  setInterval(realDate, 20000);
}

function realDate() {
  curStartTime = new Date(curEndTime);
  var now = new Date();
  curEndTime = new Date(curStartTime.format('yyyy-MM-dd') + new Date(now - 30000).format(' hh:mm:ss'));
  curEndTime = curEndTime.format('yyyy-MM-dd hh:mm:ss');
  curStartTime = curStartTime.format('yyyy-MM-dd hh:mm:ss');
  ajax({
    url: `data/${section_id}/${curStartTime}/${curEndTime}`,
    async: true,
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
    }
  })
}


/**
 * 加载数据，直到有机械信息为止
 */
function loadData() {
  curStartTime = start_date;
  curEndTime = new Date();
  curEndTime.setTime(curEndTime.getTime() - 30 * 1000); // 延迟30s
  curEndTime = curEndTime.format('yyyy-MM-dd hh:mm:ss');

  ajax({
    url: `data/${section_id}/${curStartTime}/${curEndTime}`,
    async: true,
    success: function (res) {
      machine_info = res.obj.device;
      allInfoData = res.obj.gps;
      if ($.isEmptyObject(machine_info) || $.isEmptyObject(allInfoData)) {
        loadData();
      } else {
        loadMachineTable(machine_info);

        // 初始化压实数据
        initCompactData(allInfoData);
      }
    },
  })
}