<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import * as echarts from "echarts";
require("echarts/theme/macarons"); // echarts theme
import resize from "./mixins/resize";
import { getlinedata } from "@/api/supply";

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
      default: "600px",
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
      data: "",
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
    getlinedata().then((res) => {
      var arr = [];
      arr[0] = ["进货量", "药品名称"];
      for (var i = 0; i < res.data.alldata.length; i++) {
        arr.push([res.data.alldata[i].count, res.data.alldata[i].drugname]);
      }
      this.data = arr;
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
    initChart() {
      this.chart = echarts.init(this.$el, "macarons");
      this.setOptions(this.chartData);
    },
    setOptions() {
      this.chart.setOption({
        dataset: {
          source: this.data,
        },
        grid: { containLabel: true },
        xAxis: { name: "进货量" },
        yAxis: { type: "category" },

        series: [
          {
            type: "bar",
            encode: {
              // Map the "amount" column to X axis.
              x: "进货量",
              // Map the "product" column to Y axis
              y: "药品名称",
            },
          },
        ],
        tooltip: {
          trigger: "axis",
          axisPointer: {
            type: "cross",
            label: {
              backgroundColor: "#6a7985",
            },
          },
        },
        graphic: {
          type: "text", // 类型：文本
          left: "center",
          top: "middle",
          silent: true, // 不响应事件
          invisible: this.data > 0, // 有数据就隐藏
          style: {
            fill: "#9d9d9d",
            fontWeight: "bold",
            text: "暂无数据",
            fontFamily: "Microsoft YaHei",
            fontSize: "25px",
          },
        },
      });
    },
  },
};
</script>
