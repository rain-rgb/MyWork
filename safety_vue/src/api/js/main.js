init();


// 开始函数
function start() {
  // 初始化canvas
  initCanvas();

  // 初始化道路信息
  // initRoadData(road_boundary, pile_num_list);

  // 初始化压实数据
  // initCompactData(allInfoData);


  // 绘制背景网格，只需要绘制一次
  drawGrid(grid_ctx);

  // 初始绘制默认放大倍数
  let defaultScale = 4*canvasOpt.factor;
  canvasOpt.scale = defaultScale;
  ctxList.forEach(function(ctx) {
    ctx.scale(defaultScale, defaultScale);
  })

  // 指定桩号居中
  stationToCenter(start_station);

  render();
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

  // 获取道路和桩号信息
  ajax({
    url: `stake/${section_id}`,
    // async: true,
    success: function(res) {
      // 解析道路边界数据
      let ptsArr = res.obj.boundary.split(',');
      ptsArr.forEach(function(pt) {
        let arr = pt.split(' ');
        road_boundary.push({north: arr[0], east: arr[1]});
      })
      // 封闭道路
      if (ptsArr[0] != ptsArr[ptsArr.length-1]) {
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
    success: function(res) {
      let colorArr = res.obj;
      var len = colorArr.length;
      if (len === 0) {
        return;
      }
      colorArr.sort(function(a, b) {
        return b.number - a.number;
      })
      setTimesColorBox(colorArr);

      timesColor = {
        minTimes: colorArr[len - 1].number,
        maxTimes: colorArr[0].number,
        color: [],
      }
      colorArr.forEach(function(item) {
        timesColor.color.unshift(hexToRGB(item.color));
      })


    }
  });

  // 获取压实轨迹信息
  let layerIndex = null;
  ajax({
    url: `data/${section_id}/${start_date}/${end_date}`,
    async: true,
    beforeSend: function() {
      layerIndex = layer.load(1);
    },
    complete: function() {
      layer.close(layerIndex);
    },
    success: function(res) {
      machine_info = res.obj.device;
      allInfoData = res.obj.gps;

      // 初始化压实数据
      initCompactData(allInfoData);

      closeCalTrackWorker();
    },
  })

  start();
}