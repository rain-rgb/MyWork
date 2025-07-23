export const timeNumber = () => {
    let today = new Date();
    let date =
      today.getFullYear() +
      "-" +
      twoDigits(today.getMonth() + 1) +
      "-" +
      twoDigits(today.getDate());
    let time =
      twoDigits(today.getHours()) +
      ":" +
      twoDigits(today.getMinutes()) +
      ":" +
      twoDigits(today.getSeconds());
    return date + "  " + time;
  };
  export const twoDigits = (val) => {
    if (val < 10) return "0" + val;
    return val;
  };
  // 一维数组变二维数组方法
  export const setCarouselData = (arr, size) => {
    // arr是一维数组 size是二维数组包含几条数据
    var arr2 = [];
    for (var i = 0; i < arr.length; i = i + size) {
      arr2.push(arr.slice(i, i + size));
    }
    return arr2; // 新的二维数组
  };
  //  判断时间差是否大于指定的分钟数
  export const checkTimeDifference = (time, val) => {
    // 将输入的时间字符串转换为 Date 对象
    const inputDateTime = new Date(time.replace(' ', 'T')); // 替换空格为'T'以符合ISO 8601格式
    const currentTime = new Date();
  
    // 检查输入时间是否有效
    if (isNaN(inputDateTime.getTime())) {
      this.result = '输入的时间格式无效';
      return;
    }
  
    // 计算时间差（以毫秒为单位）
    const timeDifference = Math.abs(currentTime - inputDateTime);
  
    // 将时间差转换为分钟
    const differenceInMinutes = timeDifference / (1000 * 60);
  
    // 判断时间差是否大于指定的分钟数
    return differenceInMinutes > val;
    // if (differenceInMinutes > val) {
    //   this.result = '距离当前时间大于 ' + val + ' 分钟';
    // } else {
    //   this.result = '距离当前时间小于或等于 ' + val + ' 分钟';
    // }
  };
  
  //获取当天（当日）日期   
  /**
   * @param 'YYYY-MM-DD HH:mm:ss' 'YYYY-MM-DD' 'YYYY-MM' 'HH:mm:ss' 'HH:mm' 
   * @returns 
   */
  export const formatCurrentDate = (formatString) => {
    const date = new Date();
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
  
    if (formatString === 'YYYY-MM-DD HH:mm:ss') {
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    } else if (formatString === 'YYYY-MM-DD') {
      return `${year}-${month}-${day}`;
    } else if (formatString === 'YYYY-MM') {
      return `${year}-${month}`;
    } else if (formatString === 'HH:mm:ss') {
      return `${hours}:${minutes}:${seconds}`;
    } else if (formatString === 'HH:mm') {
      return `${hours}:${minutes}`;
    }
    return ''; // Return empty string if format is not recognized
  }
  
  
  // 获取指定偏移的日期（当天或以前/以后），并按指定格式返回   (获取日期)
  /**
   * @param {number} offsetDays - 偏移天数，0 为当天，负数为以前，正数为以后
   * @param {'YYYY-MM-DD HH:mm:ss' | 'YYYY-MM-DD' | 'YYYY-MM' | 'HH:mm:ss' | 'HH:mm'} formatString - 日期格式
   * @returns {string} - 格式化后的日期字符串
   */
  export const formatDateWithOffset = (formatString,offsetDays) => {
    const date = new Date();
    date.setDate(date.getDate() + offsetDays); // 根据偏移天数调整日期
   
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
   
    if (formatString === 'YYYY-MM-DD HH:mm:ss') {
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
    } else if (formatString === 'YYYY-MM-DD') {
      return `${year}-${month}-${day}`;
    } else if (formatString === 'YYYY-MM') {
      return `${year}-${month}`;
    } else if (formatString === 'HH:mm:ss') {
      return `${hours}:${minutes}:${seconds}`;
    } else if (formatString === 'HH:mm') {
      return `${hours}:${minutes}`;
    }
    return ''; // 如果格式未识别，返回空字符串
  }
  
  
  export function getTwoHoursAgo() {
      const now = new Date();
      const twoHoursAgo = new Date(now.getTime() - 2 * 60 * 60 * 1000); // 减去两小时的毫秒数
  
      const year = twoHoursAgo.getFullYear();
      const month = String(twoHoursAgo.getMonth() + 1).padStart(2, '0');
      const day = String(twoHoursAgo.getDate()).padStart(2, '0');
      const hours = String(twoHoursAgo.getHours()).padStart(2, '0');
      const minutes = String(twoHoursAgo.getMinutes()).padStart(2, '0');
      const seconds = String(twoHoursAgo.getSeconds()).padStart(2, '0');
  
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`;
  };