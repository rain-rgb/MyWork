// 绘制渲染
function render() {
  // 绘制前先清除画布内容
  ctxList.forEach(function(ctx) {
    clearCanvas(ctx);
  })

  // 绘制压实轨迹
  compackWord();

  // 绘制道路和桩号
  drawRoad();

  // 绘制机械
  if (typeof drawMachine == 'function') {
    drawMachine();
  }

  // 设置进度条进度
  setSliderVal();

  // 更新时间
  updateTime();

  window.requestAnimFrame(render);
}