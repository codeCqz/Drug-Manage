<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import * as echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import resize from "./mixins/resize";

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
      default: "350px",
    },
    autoResize: {
      type: Boolean,
      default: true,
    },
    chartData: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      chart: null,
      week: [],
    };
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val);
      },
    },
  },
  mounted() {
    this.getWeek();
    this.$nextTick(() => {
      this.initChart();
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
      this.setOptions(this.chartData);
    },
    setOptions({ actualData } = {}) {
      this.chart.setOption({
        xAxis: {
          data: this.week,
          boundaryGap: false,
          axisTick: {
            show: false,
          },
        },
        grid: {
          left: 20,
          right: 20,
          bottom: 20,
          top: 30,
          containLabel: true,
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
          },
          padding: [5, 10],
        },
        yAxis: {
          axisTick: {
            show: false,
          },
        },
        legend: {
          data: ["actual"],
        },
        series: [
          {
            name: "真实数据",
            smooth: true,
            type: "line",
            itemStyle: {
              normal: {
                color: "#3888fa",
                lineStyle: {
                  color: "#3888fa",
                  width: 2,
                },
                areaStyle: {
                  color: "#f3f8ff",
                },
              },
            },
            data: actualData,
            animationDuration: 2800,
            animationEasing: "quadraticOut",
          },
        ],
        // graphic: {
        //   type: "text", // 类型：文本
        //   left: "center",
        //   top: "middle",
        //   silent: true, // 不响应事件
        //   invisible: this.chartData > 0, // 有数据就隐藏
        //   style: {
        //     fill: "#9d9d9d",
        //     fontWeight: "bold",
        //     text: "暂无数据",
        //     fontFamily: "Microsoft YaHei",
        //     fontSize: "25px",
        //   },
        // },
      });
    },
  },
};
</script>
