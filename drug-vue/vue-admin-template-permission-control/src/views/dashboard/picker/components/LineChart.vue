<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import * as echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import resize from "./mixins/resize";
import { fetchpickdrug } from "@/api/pickdrug";

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
      data: [],
      week: [],
      bardata: [],
    };
  },
  mounted() {
    // this.getWeek();
    fetchpickdrug().then((res) => {
      console.log(res.data.bardata);
      this.bardata = res.data.bardata;
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
    // getWeek() {
    //   var date = new Date();
    //   var week = date.getDay();
    //   var arr = ["星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"];
    //   if (week === 0) {
    //     this.week = arr;
    //   } else {
    //     for (var i = 0; i < week; i++) {
    //       var temp = arr[0];
    //       arr.splice(0, 1);
    //       arr.push(temp);
    //     }
    //     this.week = arr;
    //   }
    // },
    initChart() {
      this.chart = echarts.init(this.$el, "macarons");
      this.setOptions(this.chartData);
    },
    setOptions() {
      this.chart.setOption({
        dataset: [
          {
            dimensions: ["drugname", "handler", "druguser", "quantity", "date"],
            source: this.bardata,
          },
          {
            transform: {
              type: "sort",
              config: { dimension: "quantity", order: "desc" },
            },
          },
        ],
        xAxis: {
          type: "category",
          axisLabel: { interval: 0, rotate: 30 },
        },
        yAxis: {},
        series: {
          type: "bar",
          encode: { x: "drugname", y: "quantity" },
          datasetIndex: 1,
          itemStyle: {
            normal: {
              label: {
                show: true, //开启显示
                position: "top", //在上方显示
                textStyle: {
                  //数值样式
                  color: "#00cccc",
                  fontSize: 12,
                },
              },
            },
          },
        },
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
            label: {
              backgroundColor: "#6a7985",
            },
          },
        },
      });
    },
  },
};
</script>
