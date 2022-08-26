<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import * as echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import resize from "./mixins/resize";
import { getRadder } from "@/api/supply";
const animationDuration = 3000;

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
      radderdata: [],
      data: [],
      isShow: "",
    };
  },
  created() {
    this.getRadder();
  },
  mounted() {},
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    getRadder() {
      getRadder().then((res) => {
        this.isShow = res.data.isShow;

        var arr = [];
        for (var i = 0; i < res.data.radder.length; i++) {
          let em = res.data.radder[i];
          arr[i] = { name: em };
        }
        let arr2 = [
          {
            value: res.data.everyExp,
            name: "过期药品量",
            max: 20,
          },
          {
            value: res.data.everyDrug,
            name: "药品量",
            max: 2000,
          },
          {
            value: res.data.everySupply,
            name: "订单量",
            max: 50,
          },
        ];
        this.data = arr2;
        this.radderdata = arr;
        this.initChart();
      });
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
        radar: {
          radius: "50%",
          center: ["50%", "45%"],
          splitNumber: 8,
          splitArea: {
            areaStyle: {
              color: "rgba(127,95,132,.3)",
              opacity: 1,
              shadowBlur: 45,
              shadowColor: "rgba(0,0,0,.5)",
              shadowOffsetX: 0,
              shadowOffsetY: 15,
            },
          },
          indicator: this.radderdata,
        },
        legend: {
          left: "center",
          bottom: "10",
          data: ["过期药品量", "药品量", "订单量"],
        },
        series: [
          {
            type: "radar",
            symbolSize: 0,
            areaStyle: {
              normal: {
                shadowBlur: 13,
                shadowColor: "rgba(0,0,0,.2)",
                shadowOffsetX: 0,
                shadowOffsetY: 10,
                opacity: 1,
              },
            },
            data: this.data,
            animationDuration: animationDuration,
          },
        ],
      });
    },
  },
};
</script>
