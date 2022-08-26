<template>
  <div class="app-container">
    <upload-excel-component :on-success="handleSuccess" :before-upload="beforeUpload" />
    <el-table
      :data="tableData"
      border
      highlight-current-row
      style="width: 100%; margin-top: 20px"
    >
      <el-table-column
        v-for="item of tableHeader"
        :key="item"
        :prop="item"
        :label="item"
      />
    </el-table>
  </div>
</template>

<script>
import UploadExcelComponent from "@/components/UploadExcel/index.vue";
import { insertSupply } from "@/api/supply";
export default {
  name: "UploadExcel",
  components: { UploadExcelComponent },
  data() {
    return {
      tableData: [],
      tableHeader: [],
      newTable: {},
      queryTable: [],
    };
  },
  methods: {
    beforeUpload(file) {
      const isLt1M = file.size / 1024 / 1024 < 1;

      if (isLt1M) {
        return true;
      }

      this.$message({
        message: "请不要上传超过1MB的文件!",
        type: "warning",
      });
      return false;
    },
    handleSuccess({ results, header }) {
      this.tableHeader = header;
      this.tableData = results;

      for (let index = 0; index < results.length; index++) {
        this.newTable.supplyid = results[index].订单编号;
        this.newTable.dealerid = results[index].供销商编号;
        this.newTable.dealername = results[index].供销商名称;
        this.newTable.drugid = results[index].药品编号;
        this.newTable.drugname = results[index].药品名称;
        this.newTable.quantity = results[index].数量;
        this.newTable.handler = results[index].操作者ID;
        this.newTable.status = results[index].药品状态;
        this.newTable.exp = results[index].药品过期时间;
        this.newTable.univalent = results[index].药品单价;
        this.newTable.TP = results[index].药品总价;
        this.newTable.isUpload = true;
        this.queryTable[index] = this.newTable;
        this.newTable = {};
      }
      let index = 0;
      let copy = -1;
      while (index < this.queryTable.length) {
        if (index < this.queryTable.length) {
          if (copy == index) {
            continue;
          } else {
            copy = index;
            insertSupply(this.queryTable[index]).then(index++);
          }
        } else {
          break;
        }
      }
    },
  },
};
</script>
