<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.dealerid"
        placeholder="编号"
        style="width: 200px"
        class="filter-item"
      />
      <el-input
        v-model="listQuery.dealername"
        placeholder="名称"
        style="width: 200px; margin-left: 10px"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />

      <el-cascader
        size="large"
        placeholder="区域"
        clearable
        :options="options"
        v-model="selectOptions"
        style="margin-left: 10px"
        @change="handleChangeSearch"
      >
      </el-cascader>

      <!-- 下面是三个功能按钮 -->
      <el-button
        v-waves
        class="filter-item"
        style="margin-left: 10px"
        type="primary"
        icon="el-icon-search"
        @click="search"
      >
        搜索
      </el-button>
      <div v-if="this.$store.getters.role.role != 'admin'" style="display: inline-block">
        <el-button
          class="filter-item"
          style="margin-left: 10px"
          type="primary"
          icon="el-icon-edit"
          @click="handleCreate"
        >
          添加
        </el-button>

        <el-button
          v-waves
          :loading="downloadLoading"
          style="margin-left: 10px"
          class="filter-item"
          type="primary"
          icon="el-icon-download"
          @click="handleDownload"
        >
          导出本页
        </el-button>

        <el-button
          v-waves
          :loading="downloadLoading"
          style="margin-left: 10px"
          class="filter-item"
          type="primary"
          icon="el-icon-download"
          @click="handleDownloadAll"
        >
          导出全部
        </el-button>
      </div>
    </div>
    <!-- 上面是搜索框、添加、导出的按钮 -->

    <!-- 下面是表格内容 -->
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%; margin-top: 30px"
      @sort-change="sortChange"
    >
      <el-table-column
        label="编号"
        prop="dealerid"
        sortable="custom"
        align="center"
        width="80"
        :class-name="getSortClass('dealerid')"
      >
        <template slot-scope="{ row }">
          <span>{{ row.dealerid }}</span>
        </template>
      </el-table-column>
      <el-table-column label="名称" width="150px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.dealername }}</span>
        </template>
      </el-table-column>
      <el-table-column label="区域" min-width="150px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.area }}</span>
        </template>
      </el-table-column>
      <el-table-column label="详细地址" width="200px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.address }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机号" width="110px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.telephone }}</span>
        </template>
      </el-table-column>

      <el-table-column label="联系人" width="110px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.contacts }}</span>
        </template>
      </el-table-column>
      <el-table-column label="银行名称" width="110px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.bank }}</span>
        </template>
      </el-table-column>
      <el-table-column label="银行卡号" width="110px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.account }}</span>
        </template>
      </el-table-column>
      <el-table-column
        label="操作"
        align="center"
        width="230"
        class-name="small-padding fixed-width"
        v-if="this.$store.getters.role.role != 'admin'"
      >
        <template slot-scope="{ row, $index }">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            修改
          </el-button>
          <el-button
            v-if="row.status != 'deleted'"
            size="mini"
            type="danger"
            @click="beforeDelete(row, $index)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 下面是elmentui的分页实现 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <!-- 添加时弹出的框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="120px"
        style="width: 460px; margin-left: 50px"
      >
        <el-form-item label="供销商名称" prop="dealername">
          <el-input v-model="temp.dealername" />
        </el-form-item>
        <el-form-item label="区域" prop="selectedOptions">
          <el-cascader
            size="large"
            clearable
            :options="options"
            v-model="selectedOptions"
            @change="handleChange"
            @blur="areaBlur"
          >
          </el-cascader>
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="temp.address" />
        </el-form-item>
        <el-form-item label="手机号" prop="telephone">
          <el-input v-model="temp.telephone" />
        </el-form-item>
        <el-form-item label="联系人" prop="contacts">
          <el-input v-model="temp.contacts" />
        </el-form-item>

        <el-form-item label="银行卡号" prop="account">
          <el-input v-model="temp.account" />
        </el-form-item>
        <el-form-item label="银行名称" prop="bank">
          <el-input v-model="temp.bank" :disabled="true" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="createData"> 确定 </el-button>
        <el-button @click="cancel"> 取消 </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, fetchDeriveList, fetchSearchList } from "@/api/dealer";
import waves from "@/directive/waves"; // waves directive
import { regionData, CodeToText, TextToCode } from "element-china-area-data";
import Pagination from "@/components/Pagination"; // secondary package based on el-pagination

export default {
  name: "ComplexTable",
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: "success",
        draft: "info",
        deleted: "danger",
      };
      return statusMap[status];
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type];
    },
  },
  data() {
    const checkArea = (rule, value, callback) => {
      if (!this.selectedOptions || this.selectedOptions.length <= 0) {
        return callback(new Error("请选择区域！"));
      }
      return callback();
    };
    const checkTelephone = (rule, value, callback) => {
      if (!this.temp.telephone || this.temp.telephone.length <= 0) {
        return callback(new Error("请填入手机号！"));
      } else if (this.temp.telephone.length < 11 || this.temp.telephone.length > 11) {
        return callback(new Error("请填入正确的手机号！"));
      }else {
          var f =  Number(this.temp.telephone)
          if(!isNaN(f)){
            return callback();
          }else{
            return callback(new Error("请填入正确的手机号！"));
          }
        }
    };
    const checkAccount = (rule, value, callback) => {
      if (!this.temp.account || this.temp.account.length <= 0) {
        return callback(new Error("请填入银行卡号！"));
      } else if (this.temp.account.length < 16 || this.temp.account.length > 19) {
        return callback(new Error("请填入正确的银行卡号！"));
      } else if (!this.temp.bank || this.temp.bank.length <= 0) {
        return callback(new Error("请填入正确的银行卡号！"));
      }
      return callback();
    };
    const checkCon = (rule, value, callback) => {
      if (!this.temp.contacts || this.temp.contacts.length <= 0) {
        return callback(new Error("请填入联系人！"));
      } else{
        var reg = new RegExp("[\\u4E00-\\u9FFF]+$","g");
        if(!reg.test(this.temp.contacts)){
          return callback(new Error("请填入正确的联系人！"));
        }else{
          return callback();
        }
      }
      return callback();
    };

    return {
      referenceList: [],
      dealerid: "",
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: false,

      listQuery: {
        dealerid: "",
        dealername: "",
        area: "",
        page: 1,
        limit: 10,
        sort: "+dealerid",
      },
      options: regionData,
      selectOptions: [],
      selectedOptions: [],
      importanceOptions: [1, 2, 3],
      statusOptions: ["published", "draft", "deleted"],
      showReviewer: false,
      temp: {
        dealerid: "",
        dealername: "",
        area: "",
        address: "",
        telephone: "",
        contacts: "",
        bank: "",
        account: "",
      },
      dialogFormVisible: false,
      dialogStatus: "",
      textMap: {
        update: "修改供销商信息",
        create: "添加供销商信息",
      },
      dialogPvVisible: false,
      pvData: [],
      role: "",
      rules: {
        dealername: [{ required: true, message: "请填入供销商名称", trigger: "blur" }],
        selectedOptions: [
          { type: "array", required: true, trigger: "change", validator: checkArea },
        ],
        address: [{ required: true, message: "请填入地址", trigger: "blur" }],
        telephone: [{ required: true, trigger: "blur", validator: checkTelephone }],
        contacts: [{ required: true, trigger: "blur", validator: checkCon}],
        account: [{ required: true, trigger: "blur", validator: checkAccount }],
        bank: [{ required: true, message: "请填入正确的银行卡号", trigger: "change" }],
      },
      downloadLoading: false,
    };
  },
  created() {
    this.getList();
  },
  watch: {
    temp: {
      handler: function (val) {
        //可以做些相应的处理
        let account = val.account;
        if (account >= 16) {
          var BIN = require("bankcardinfo");
          //callback 方式调用
          BIN.getBankBin(account, function (err, data) {
            if (!err) {
              val.bank = data.bankName;
            } else {
              val.bank = "";
            }
          });
        } else if (account <= 16) {
          val.bank = "";
        }
      },
      deep: true,
    },
  },
  methods: {
    beforeDelete(row, index) {
      this.$confirm("此操作将删除记录，确认删除吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "error",
      }).then(() => {
        this.handleDelete(row, index);
      });
    },
    areaBlur() {
      this.$refs.dataForm.validateField("selectedOptions");
    },
    cancel() {
      this.dialogFormVisible = false;
      this.selectedOptions = [""];
      this.resetTemp;
    },
    handleChangeSearch() {
      if (this.selectOptions != "") {
        let value1 = [
          this.selectOptions[0],
          this.selectOptions[1],
          this.selectOptions[2],
        ];
        let area =
          CodeToText[value1[0]] +
          "/" +
          CodeToText[value1[1]] +
          "/" +
          CodeToText[value1[2]];
        this.listQuery.area = area;
      } else {
        this.listQuery.area = "";
      }
    },
    handleChange() {
      let value1 = [
        this.selectedOptions[0],
        this.selectedOptions[1],
        this.selectedOptions[2],
      ];
      let area =
        CodeToText[value1[0]] + "/" + CodeToText[value1[1]] + "/" + CodeToText[value1[2]];
      this.temp.area = area;
    },
    getList() {
      this.listLoading = false;
      if (
        this.listQuery.dealerid != "" ||
        this.listQuery.dealername != "" ||
        this.listQuery.area != ""
      ) {
        this.search();
      } else {
        fetchList(this.listQuery).then((response) => {
          this.referenceList = response.data.items;
          const replaceList = this.forReplace(
            JSON.parse(JSON.stringify(response.data.items))
          );
          this.list = replaceList;
          this.total = response.data.total;

          setTimeout(() => {
            this.listLoading = false;
          }, 1.5 * 1000);
        });
      }
    },
    forReplace(data) {
      for (let i = 0; i < data.length; i++) {
        let area = String(data[i].area).replaceAll("/", "");
        data[i].area = area;
      }
      return data;
    },

    handleFilter() {
      this.listQuery.page = 1;
      this.selectedOptions = [];
      this.getList();
    },
    search() {
      //fetchSearchList
      fetchSearchList(this.listQuery).then((response) => {
        this.referenceList = response.data.items;
        if (response.data.total != 0) {
        } else {
          this.$notify({
            title: "提示",
            message: "未搜索到",
            type: "warning",
            duration: 2000,
          });
        }
        const replaceList = this.forReplace(
          JSON.parse(JSON.stringify(response.data.items))
        );
        this.list = replaceList;
        this.total = response.data.total;
      });
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: "操作Success",
        type: "success",
      });
      row.status = status;
    },
    sortChange(data) {
      const { prop, order } = data;
      if (prop === "dealerid") {
        this.sortByID(order);
      }
    },
    sortByID(order) {
      if (order === "ascending") {
        this.listQuery.sort = "+dealerid";
      } else {
        this.listQuery.sort = "-dealerid";
      }
      this.handleFilter();
    },
    resetTemp() {
      this.temp = {
        dealerid: "",
        dealername: "",
        area: "",
        address: "",
        telephone: "",
        contacts: "",
        bank: "",
        account: "",
      };
    },

    handleCreate() {
      this.resetTemp();
      this.selectedOptions = [];
      this.selectOptions = [];
      this.dialogStatus = "create";
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },
    createData() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          this.dialogFormVisible = false;

          if (this.dialogStatus === "update") {
            if (this.temp.area.indexOf("/") === -1) {
              for (var i = 0; i < this.referenceList.length; i++) {
                if (this.referenceList[i].dealerid === this.temp.dealerid) {
                  this.temp.area = this.referenceList[i].area;
                }
              }
            }
            this.$store.dispatch("dealer/editDealer", this.temp).then(() => {
              this.getList();
              this.$notify({
                title: "修改成功",
                message: "修改成功",
                type: "success",
                duration: 2000,
              });
            });
            this.selectedOptions = [""];
          } else if (this.dialogStatus === "create") {
            this.$store.dispatch("dealer/insertDealer", this.temp).then(() => {
              this.getList();
              this.$notify({
                title: "添加成功",
                message: "添加成功",
                type: "success",
                duration: 2000,
              });
            });
            this.selectedOptions = [""];
          }
        }
      });
    },
    handleUpdate(row) {
      this.selectedOptions = [""];
      this.temp = Object.assign({}, row); // copy obj
      for (var i = 0; i < this.referenceList.length; i++) {
        if (this.referenceList[i].dealerid === row.dealerid) {
          var area = this.referenceList[i].area.split("/");
        }
      }
      this.selectedOptions = TextToCode[area[0]][area[1]][area[2]].code;
      this.dialogStatus = "update";
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },

    handleDelete(row, index) {
      this.list.splice(index, 1);
      this.dealerid = row.dealerid;
      this.$store.dispatch("dealer/deleteDealer", this.dealerid).then(() => {
        this.getList();
        this.$notify({
          title: "成功",
          message: "删除成功",
          type: "success",
          duration: 2000,
        });
      });
    },
    handleFetchPv(pv) {
      fetchPv(pv).then((response) => {
        this.pvData = response.data.pvData;
        this.dialogPvVisible = true;
      });
    },
    handleDownload() {
      import("@/vendor/Export2Excel").then((excel) => {
        const tHeader = [
          "编号",
          "名称",
          "区域",
          "详细地址",
          "手机号",
          "联系人",
          "银行名称",
          "银行卡号",
        ];
        const filterVal = [
          "dealerid",
          "dealername",
          "area",
          "address",
          "telephone",
          "contacts",
          "bank",
          "account",
        ];
        const listdata = this.list;
        const data = this.formatJson(filterVal, listdata);
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: "供销商表-第" + this.listQuery.page + "页",
        });
      });
    },

    handleDownloadAll() {
      fetchDeriveList(this.listQuery).then((response) => {
        import("@/vendor/Export2Excel").then((excel) => {
          const tHeader = [
            "编号",
            "名称",
            "区域",
            "详细地址",
            "手机号",
            "联系人",
            "银行名称",
            "银行卡号",
          ];
          const filterVal = [
            "dealerid",
            "dealername",
            "area",
            "address",
            "telephone",
            "contacts",
            "bank",
            "account",
          ];
          const listdata = response.data.items;
          const data = this.formatJson(filterVal, listdata);
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: "供销商表(全部)",
          });
          // this.downloadLoading = true
        });
      });
    },

    formatJson(filterVal, listdata) {
      return listdata.map((v) => filterVal.map((j) => v[j]));
    },
    getSortClass: function (key) {
      const sort = this.listQuery.sort;
      return sort === `+${key}` ? "ascending" : "descending";
    },
  },
};
</script>
