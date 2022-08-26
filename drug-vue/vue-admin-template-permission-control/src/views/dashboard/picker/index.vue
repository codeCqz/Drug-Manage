<template>
  <div class="dashboard-editor-container">
    <!-- <panel-group @handleSetLineChartData="handleSetLineChartData" /> -->

    <el-row style="background: #fff; padding: 16px 16px 0; margin-bottom: 32px">
      <line-chart :chart-data="lineChartData" />
    </el-row>

    <!-- <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <raddar-chart />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pie-chart />
        </div>
      </el-col> 
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <bar-chart />
        </div>
      </el-col> 
  </el-row> -->
  </div>
</template>

<script>
import PanelGroup from "./components/PanelGroup";
import LineChart from "./components/LineChart";
import RaddarChart from "./components/RaddarChart";
import PieChart from "./components/PieChart";
import BarChart from "./components/BarChart";
import { getDrugClass, getDayAdd, getAllExpire, getAllCount } from "@/api/supply";

const lineChartData = {
  classCount: {
    actualData: [0, 0, 0, 0, 0, 0, 0],
  },
  dayadd: {
    actualData: [0, 0, 0, 0, 0, 0, 0],
  },
  expire: {
    actualData: [0, 0, 0, 0, 0, 0, 0],
  },
  allcount: {
    actualData: [0, 0, 0, 0, 0, 0, 0],
  },
};

export default {
  name: "DashboardAdmin",
  components: {
    PanelGroup,
    LineChart,
    RaddarChart,
    PieChart,
    BarChart,
  },
  data() {
    return {
      lineChartData: lineChartData.classCount,
    };
  },
  mounted() {
    this.getClassCount();
    this.getDayAdd();
    this.getAllExpire();
    this.getAllCount();
  },
  methods: {
    handleSetLineChartData(type) {
      this.lineChartData = lineChartData[type];
    },
    getClassCount() {
      getDrugClass(this.$store.getters.roles).then((res) => {
        lineChartData.classCount.actualData = res.data.drugclass;
      });
    },
    getDayAdd() {
      getDayAdd(this.$store.getters.roles).then((res) => {
        lineChartData.dayadd.actualData = res.data.dayadd;
      });
    },
    getAllExpire() {
      getAllExpire(this.$store.getters.roles).then((res) => {
        lineChartData.expire.actualData = res.data.expire;
      });
    },
    getAllCount() {
      getAllCount(this.$store.getters.roles).then((res) => {
        lineChartData.allcount.actualData = res.data.allcount;
      });
    },
  },
};
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width: 1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
