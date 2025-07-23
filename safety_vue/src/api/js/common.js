/**
 * 桩号数字转换
 * @param prefix
 * @param stationNum
 */
function formatStation(prefix, stationNum) {
  let k = Math.floor(stationNum / 1000);
  let r = (stationNum % 1000).toFixed(2);
  return prefix + k + '+' + r;
}

/**
 * 16进制颜色值字符串转换为rgb数组
 * @param hex
 * @returns {number[]}
 */
function hexToRGB(hex) {
  var reg = /^#([0-9a-fA-f]{3}|[0-9a-fA-f]{6})$/;
  if (reg.test(hex)) {
    hex = RegExp.$1;
    var R = parseInt('0x' + hex.slice(0,2));
    var G = parseInt('0x' + hex.slice(2,4));
    var B = parseInt('0x' + hex.slice(4,6));
    return [R, G, B];
  } else {
    return [0,0,0];
  }
}

/**
 * 二分查找法，查找pts中time ≤ time的索引
 * @param pts
 * @param time
 */
function lower_index(pts, time) {
  var len = pts.length;
  var left = 0, right = len - 1;
  var mid;
  while (left < right) {
    mid = Math.floor((left + right + 1) / 2);
    var curTime = pts[mid].gpsTime;
    if (curTime == time) {
      return mid;
    } else if (curTime < time) {
      left = mid;
    } else {
      right = mid - 1;
    }
  }
  return left;
}

function notContinue(pts) {
  let count = 0;
  for (let i = 1; i < pts.length; i++) {
    if (pts[i].gpsTime - pts[i-1].gpsTime > 1000) {
      console.log(pts[i-1].gpsTime, pts[i].gpsTime);
      count++;
    }
  }
  console.log(count);
}

function isSort(pts) {
  for (let i = 1; i < pts.length; i++) {
    if (pts[i].gpsTime < pts[i-1].gpsTime) {
      console.log('not sort');
      return;
    }
  }
  console.log('sort');
}