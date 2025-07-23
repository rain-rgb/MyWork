/**
 * canvas相关的设置
 */

let body = document.querySelector('body');

// 记录canvas的一些信息
let canvasOpt = {
  height: body.clientHeight,  // canvas元素的宽高
  width: body.clientWidth,
  scale: 1, // 当前canvas的缩放比例
  factor: 1, // 将工程坐标范围映射到canvas视口范围的比例
  originScreen: {x:0, y: 0}, // canvas原点（0,0）的屏幕坐标
}

// canvas元素
let grid_cv = this.$('#grid')[0];  // 网格背景canvas
let main_cv = this.$('#main')[0];  // 主视图canvas
let event_cv = this.$('#event')[0]; // 事件canvas
let buffer_cv = document.createElement('canvas'); // 离屏缓存canvas
let cvList = [grid_cv, main_cv, event_cv, buffer_cv];


// canvas绘图上下文CanvasRenderingContext2D
let grid_ctx = grid_cv.getContext('2d');
let main_ctx = main_cv.getContext('2d');
let event_ctx = event_cv.getContext('2d');
let buffer_ctx = buffer_cv.getContext('2d');
let ctxList = [main_ctx, event_ctx, buffer_ctx];

function initCanvas() {
  // 设置canvas的宽高
  cvList.forEach(function(cv) {
    cv.height = canvasOpt.height;
    cv.width = canvasOpt.width;
  })

  // 设置canvas层级
  grid_cv.style["z-index"] = 0;
  event_cv.style["z-index"] = 99;
}

// 事件处理
// 鼠标滚轮缩放
event_cv.onwheel = event_cv.onmousewheel = function (e) {
  e.preventDefault();
  let scaleFlag = e.wheelDelta > 0 ? 1 : -1;
  let scale = Math.exp(scaleFactor * scaleFlag);

  // 缩放到一定程度就不允许缩放了
  if (scale * canvasOpt.scale < 1) {
    // 不能缩小到比原始比例小
    scale = 1 / canvasOpt.scale;
  }
  if (scale * canvasOpt.scale > 100 * canvasOpt.factor) {
    // 1px代表0.01米时
    return;
  }

  let canvasPt = client2canvas(event_ctx, e.offsetX, e.offsetY);
  // 缩放后，当前点的偏移
  let offsetX = (scale - 1) * canvasPt.x;
  let offsetY = (scale - 1) * canvasPt.y;
  ctxList.forEach(function(ctx) {
    ctx.transform(scale, 0, 0, scale, 0 - offsetX, 0 - offsetY);
  })
  canvasOpt.originScreen = canvas2client(event_ctx, 0, 0);
  canvasOpt.scale = event_ctx.getTransform().a;
}

// 鼠标拖拽平移
event_cv.onmousedown = function (e) {
  let bDrag = true;
  let startPt = client2canvas(event_ctx, e.offsetX, e.offsetY);

  event_cv.style.zIndex = "99999";
  event_cv.style.cursor = "move";

  window.onmousemove = function (e) {
    if (bDrag) {
      let curPt = client2canvas(event_ctx, e.offsetX, e.offsetY);
      // 平移偏移量
      let offsetX = (curPt.x - startPt.x);
      let offsetY = (curPt.y - startPt.y);
      ctxList.forEach(function(ctx) {
        ctx.translate(offsetX, offsetY);
      })
      canvasOpt.originScreen = canvas2client(event_ctx, 0, 0);
    }
  }

  window.onmouseup = function (e) {
    bDrag = false;

    window.onmousemove = null;
    window.onmouseup = null;
    event_cv.style.cursor = "default";
    event_cv.style.zIndex = "99";
  }
}

// 设置压实遍数颜色块展示
function setTimesColorBox(colorArr) {
  var el = this.$('.time-color');
  var domstr = '';
  let len = colorArr.length;
  colorArr.forEach(function(cl, index) {
    let symbol = '=';
    if (index == 0) {
      symbol = '>=';
    }
    if (index == len - 1) {
      symbol = '<=';
    }
    domstr += `<div style="background-color: this.${cl.color};">this.${symbol}this.${cl.number}</div>`;
  })
  el.append(domstr);
}

// 右上角机械列表展示块
function loadMachineTable(device) {
  let tr = '';
  for (let key in device) {
    tr += `<tr>
        <td><a machineid="this.${key}" class="name">this.${device[key]['machine_name']}</a>
          <button class="layui-btn layui-btn-xs layui-btn-primary focus" style="float:right">跟随</button>
        </td>
        <td style="text-align: center"><input machineid="this.${key}" type="checkbox" name="show" checked lay-skin="primary"></td>
        <td style="text-align: center"><input machineid="this.${key}" type="checkbox" name="follow" lay-skin="primary"></td>
      </tr>`
  }
  this.$('#machine-list tbody').append(tr);

  // 初始化处理
  this.$('#machine-list [name=follow]')[0].checked = true;
  focus_machine = this.$('#machine-list [name=follow]:checked').attr('machineid');

  this.$('.focus').click(function() {
    this.$(this).parent().parent().find('[name=follow]').click();
  })

  // 需要居中机械改变
  this.$('#machine-list [name=follow]').click(function() {
    if (this.checked) {
      this.$('[name=follow]').not(this).prop('checked', false);
      focus_machine = this.$(this).attr('machineid');
    } else {
      focus_machine = '';
    }
  })

  // 显示改变
  this.$('#machine-list [name=show]').click(function() {
    showList = [];
    this.$('#machine-list [name=show]:checked').each(function() {
      let mid = this.$(this).attr('machineid');
      showList.push(mid);
    })
  })
}

/**
 * 初始化播放进度条
 * @param start
 * @param end
 */
function initSlider(start, end) {
  let diff = (end - start) / 1000;
  layui.use('slider', function(){
    //渲染
    sliderIns = layui.slider.render({
      elem: '#slider',
      min: 0,
      max: diff,
      tips: false,
      change: function(value) {
        curIndex = value;
      }
    });
  });

}

/**
 * 初始化数据加载进度
 */
function upDateLoadProgress() {
  let s = new Date(start_date).getTime();
  let e = new Date(end_date).getTime();
  let cur = new Date(curEndTime).getTime();
  let p = (cur - s) / (e - s) * 100;
  this.$('#mask').css({'width': p + '%'});
}

/**
 * 初始化播放按钮
 */
function initControl() {
  this.$('#control .play').click(function() {
    isPlay = !isPlay;
    let classStr = '';
    if (isPlay) {
      classStr = 'glyphicon glyphicon-pause';
      indexStep = 1;
    } else {
      classStr = 'glyphicon glyphicon-play';
      indexStep = 0; // 暂停后curIndex不增加
    }
    this.$('span', this)[0].className = classStr;
  })

  this.$('#slow, #fast, #reset').click(function() {
    let speed = getSpeed();
    if (this.id == 'slow' && speed > 1) {
      speed--;
    } else if (this.id == 'fast') {
      speed++;
    } else {
      speed = 1;
    }
    setSpeed(speed);
    setPlaySpeed(speed);
  })


  function getSpeed() {
    let speed = this.$('#control .speed').text();
    speed = speed.replace('x', '');
    return parseInt(speed);
  }

  function setSpeed(speed) {
    this.$('#control .speed').text('x' + speed);
  }
}

/**
 * 设置播放进度
 */
function setSliderVal() {
  if (sliderIns) {
    sliderIns.setValue(curIndex);
  }
}

function updateTime() {
  let d = getCurTime();
  this.$('#time p').text(d.format('yyyy-MM-dd hh:mm:ss'));
}

function getCurTime() {
  let start = Date.parse(start_date);
  let t = start + curIndex * 1000;
  let d = new Date(t);
  return d;
}