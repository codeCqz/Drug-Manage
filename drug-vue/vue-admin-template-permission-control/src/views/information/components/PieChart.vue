<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import * as echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import resize from "./mixins/resize";
import { getAllPieData } from "@/api/supply";

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
      piedata: "",
      data: [],
    };
  },
  mounted() {
    this.getAllPieData();
  },
  beforeDestroy() {
    if (!this.chart) {
      return;
    }
    this.chart.dispose();
    this.chart = null;
  },
  methods: {
    getAllPieData() {
      getAllPieData().then((res) => {
        this.piedata = res.data.allpiedata;
        var arr = [];

        for (let index = 0; index < 5; index++) {
          const element = res.data.count[index];
          var el = { value: element, name: this.piedata[index] };
          arr[index] = el;
        }
        this.data = arr;
        this.initChart();
      });
    },
    initChart() {
      this.chart = echarts.init(this.$el, "macarons");

      this.chart.setOption({
        tooltip: {
          trigger: "item",
          formatter: "{a} <br/>{b} : {c} ({d}%)",
        },
        legend: {
          left: "center",
          bottom: "10",
          data: this.piedata,
        },
        series: [
          {
            name: "总数据",
            type: "pie",
            roseType: "radius",
            radius: [15, 95],
            center: ["50%", "40%"],
            data: this.data,
            animationEasing: "cubicInOut",
            animationDuration: 2600,
          },
        ],
        // graphic: {
        //   type: "text", // 类型：文本
        //   left: "center",
        //   top: "middle",
        //   silent: true, // 不响应事件
        //   invisible: this.piedata > 0, // 有数据就隐藏
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
