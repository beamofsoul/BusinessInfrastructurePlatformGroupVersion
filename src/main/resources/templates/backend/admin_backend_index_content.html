<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>
<html class="no-js">
  <head>
    <%@ include file="../include_admin_head.html"%>
  </head>
  <body>
    <div id="chartContainer">
      <div id="barChart" style="min-height:400px;padding-top:20px"></div>
      <div id="pieChart" style="min-height:400px;padding-top:20px"></div>
    </div>

    <%@ include file="../include_list_required.html"%>

    <script src="${pageContext.request.contextPath }/static/ECHARTS/js/echarts.min.js"></script>
    <script src="${pageContext.request.contextPath }/static/ECHARTS/js/theme/macarons.js"></script>
    <script type="text/javascript">
        var barChart = echarts.init(document.getElementById('barChart'), 'macarons');
        //chart.showLoading({text: '正在努力的读取数据中...'});
        //chart.hideLoading(); //可放在ajax的success后执行，以便于使用ajax加载图表数据
        barChart.setOption({
          title: {text: '用户注册量', x: 'center'},
          tooltip: {show: true, trigger: 'axis'}, //鼠标悬停数据提示框, trigger可赋值axis或item
          //legend: {data:['系统用户注册量月历柱状图']}, //图例，data内容需要与series中name的值一致，否则legend不显示
          xAxis: [{type: 'category', data: ['一月份', '二月份', '三月份', '四月份', '五月份', '六月份', '七月份', '八月份', '九月份', '十月份', '十一月份', '十二月份']}],
          yAxis: [{type: 'value'}],
          series: [{'name': '系统用户注册量月历柱状图', 'type': 'bar', 'data': [5, 20, 40, 10, 10, 20, 27, 94, 37, 73, 8, 12]}]
        });

        var pieChart = echarts.init(document.getElementById('pieChart'), 'macarons');
        pieChart.setOption({
          title: {text: '用户访问来源', x: 'center'},
          tooltip: {trigger: 'item', formatter: "{a} <br/>{b} : {c} ({d}%)"},
          legend: {orient: 'vertical', x: 'left', data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']},
          calculable: true,
          series: [{name: '访问来源', type: 'pie', radius: '55%', center: ['50%', '60%'], data: [{value: 335, name: '直接访问'}, {value: 310, name: '邮件营销'}, {value: 234, name: '联盟广告'}, {value: 135, name: '视频广告'}, {value: 1548, name: '搜索引擎'}]}]
        });

        adaptiveIframeHeight($('#chartContainer'));
    </script>
  </body>
</html>