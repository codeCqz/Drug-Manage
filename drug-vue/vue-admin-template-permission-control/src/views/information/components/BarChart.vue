<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import * as echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import resize from "./mixins/resize";
import { getprofit } from "@/api/pickdrug";
const animationDuration = 6000;

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: "chart",
    },
    width: {
      type: String,
      default: "100%",
    },
    height: {
      type: String,
      default: "300px",
    },
  },
  data() {
    return {
      chart: null,
      week: [],
      data: {},
    };
  },
  mounted() {
    this.getWeek();
    getprofit().then((res) => {
      this.data.order = res.data.order;
      this.data.pick = res.data.pick;
      this.data.profit = res.data.profit;
      this.$nextTick(() => {
        this.initChart();
      });
    });
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    getWeek() {
      var date = new Date();
      var week = date.getDay();
      var arr = ["星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"];
      if (week === 0) {
        this.week = arr;
      } else {
        for (var i = 0; i < week; i++) {
          var temp = arr[0];
          arr.splice(0, 1);
          arr.push(temp);
        }
        this.week = arr;
      }
    },
    initChart() {
      this.chart = echarts.init(this.$el, "macarons");

      this.chart.setOption({
        tooltip: {
          trigger: "axis",
          axisPointer: {
            // 坐标轴指示器，坐标轴触发有效
            type: "shadow", // 默认为直线，可选为：'line' | 'shadow'
          },
        },
        grid: {
          top: 10,
          left: "2%",
          right: "2%",
          bottom: "3%",
          containLabel: true,
        },
        xAxis: [
          {
            type: "category",
            data: this.week,
            axisTick: {
              alignWithLabel: true,
            },
          },
        ],
        yAxis: [
          {
            type: "value",
            axisTick: {
              show: false,
            },
          },
        ],
        series: [
          {
            name: "订单金额",
            type: "bar",
            stack: "vistors",
            barWidth: "60%",
            data: this.data.order,
            animationDuration,
          },
          {
            name: "取药金额",
            type: "bar",
            stack: "vistors",
            barWidth: "60%",
            data: this.data.pick,
            animationDuration,
          },
          {
            name: "今日利润",
            type: "bar",
            stack: "vistors",
            barWidth: "60%",
            data: this.data.profit,
            animationDuration,
          },
        ],
      });
    },
  },
};
</script>
