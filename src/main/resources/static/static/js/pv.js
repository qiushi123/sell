/**
 * Created by qcl on 2018/4/14.
 * 统计每个页的pv访问量
 */
//引入别的js
// <!--获取当前ip和城市-->
// document.write("<script language=javascript src=’http://pv.sohu.com/cityjson?ie=utf-8’></script>");
<!--ajax-->
// document.write("<script language=javascript src=’/static/static/js/jquery.min.js’></script>");

var baseUrl = "";
var ipstr = "" + returnCitySN['cip'] + returnCitySN['cname']
if (window.location.hostname == "localhost") {
    baseUrl = "https://localhost:8443";
} else {
    baseUrl = "https://30paotui.com";
}
var pathname = window.location.pathname;

$(document).ready(function () {
    $.ajax({
        url: baseUrl + "/pv/2048/add",
        type: "post",
        data: {"ip": ipstr, "path": pathname},
        success: function (result) {
            console.log("数据提交成功");
        }
    });

    // $.ajax({
    //     url: baseUrl + "/pv/2048/list",
    //     success: function (result) {
    //         document.getElementById("count").innerHTML = result.data;
    //     }
    // });
});