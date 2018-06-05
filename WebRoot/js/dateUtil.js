Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
}

function dateFormat(longTypeDate){  
	var dateType = "";  
	var date = new Date();  
	date.setTime(longTypeDate); 
	dateType = date.getFullYear()+"-"+getMonth(date)+"-"+getDay(date);//yyyy-MM-dd格式日期
	return dateType;
} 
//返回 01-12 的月份值   
function getMonth(date){  
	var month = "";  
	month = date.getMonth() + 1; //getMonth()得到的月份是0-11  
	if(month<10){  
		month = "0" + month;  
	}  
	return month;  
}  
//返回01-30的日期  
function getDay(date){  
	var day = "";  
	day = date.getDate();  
	if(day<10){  
		day = "0" + day;  
	}  
	return day;  
}