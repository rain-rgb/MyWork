const date = function() {
	var d = new Date()
	var curr_date = d.getDate()
	var curr_month = d.getMonth() + 1
	var curr_year = d.getFullYear()
	String(curr_month).length < 2 ? (curr_month = "0" + curr_month) : curr_month
	String(curr_date).length < 2 ? (curr_date = "0" + curr_date) : curr_date
	var yyyyMMdd = curr_year + "-" + curr_month + "-" + curr_date
	return yyyyMMdd
}

const week = function() {
	var day = new Date().getDay()
	var weeks = new Array("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
	var week = weeks[day]
	return week
}

const time = function() {
	var d = new Date()
	var curr_hour = d.getHours()
	var curr_min = d.getMinutes()
	var curr_sec = d.getSeconds()
	String(curr_hour).length < 2 ? (curr_hour = "0" + curr_hour) : curr_hour
	String(curr_min).length < 2 ? (curr_min = "0" + curr_min) : curr_min
	String(curr_sec).length < 2 ? (curr_sec = "0" + curr_sec) : curr_sec
	var currentTime = curr_hour + ":" + curr_min + ":" + curr_sec
	return currentTime
}

const times = function(date) {
	var dd = new Date(date)
	dd.setDate(dd.getDate());
	var curr_hour = dd.getHours()
	var curr_min = dd.getMinutes()
	var curr_sec = dd.getSeconds()
	String(curr_hour).length < 2 ? (curr_hour = "0" + curr_hour) : curr_hour
	String(curr_min).length < 2 ? (curr_min = "0" + curr_min) : curr_min
	String(curr_sec).length < 2 ? (curr_sec = "0" + curr_sec) : curr_sec
	var currentTime = curr_hour + ":" + curr_min + ":" + curr_sec
	return currentTime
}

/*
 *获取指定日期的前一天，后一天
 *date 代表指定的日期，格式：2018-09-27
 *day 传-1表始前一天，传1表始后一天
 */
const getNextPreDate = function(date, day) {
	// console.log("date",date)
	var dd = new Date(date);
	dd.setDate(dd.getDate() + day);
	// console.log("dd",dd)
	var curr_date = dd.getDate()
	var curr_month = dd.getMonth() + 1
	var curr_year = dd.getFullYear()
	String(curr_month).length < 2 ? (curr_month = "0" + curr_month) : curr_month
	String(curr_date).length < 2 ? (curr_date = "0" + curr_date) : curr_date
	var yyyyMMdd = curr_year + "-" + curr_month + "-" + curr_date
	return yyyyMMdd
};

export default {
	date,
	time,
	week,
	getNextPreDate
}
