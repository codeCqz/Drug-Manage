<template>
  <div class="app-container">
    <div class="filter-container">
      <el-autocomplete
        class="inline-input"
        v-model="listQuery.drugname"
        :fetch-suggestions="querySearch"
        placeholder="请输入药品名称"
        @select="handleSelect"
        :debounce="0"
        clearable
      ></el-autocomplete>
      <el-input
        v-model="listQuery.standardcode"
        placeholder="请输入药品本位码"
        style="width: 200px; margin-left: 10px"
        class="filter-item"
        clearable
      />

      <!--      &lt;!&ndash; 下面是三个功能按钮 &ndash;&gt;-->
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

      <el-button type="primary" icon="el-icon-edit" @click="handleCreate">新增</el-button>

      <!-- <el-button
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
      </el-button> -->
    </div>
    <!-- 上面是搜索框、添加、导出的按钮 -->

    <!-- 下面是表格内容 -->
    <!-- 下面是表格内容 -->
    <!-- 下面是表格内容 -->
    <!-- 下面是表格内容 -->
    <!-- 下面是表格内容 -->
    <!-- 下面是表格内容 -->
    <!-- 下面是表格内容 -->
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
        label="药品编号"
        prop="drugid"
        sortable="custom"
        align="center"
        width="120"
        :class-name="getSortClass('drugid')"
      >
        <template slot-scope="{ row }">
          <span>{{ row.drugid }}</span>
        </template>
      </el-table-column>

      <el-table-column label="药品名称" width="260px" align="center">
        <template slot-scope="{ row }">
          <button
            @click="lookDrug(row, true)"
            style="border: none; background-color: transparent"
          >
            <span>{{ row.drugname }}</span>
          </button>
        </template>
      </el-table-column>

      <el-table-column label="批准文号" width="180px" align="center">
        <template slot-scope="{ row }">
          <span>国药准字{{ row.approvalnum }}</span>
        </template>
      </el-table-column>
      <el-table-column label="生产单位" width="280px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.company }}</span>
        </template>
      </el-table-column>
      <el-table-column label="药品本位码" width="210px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.standardcode }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        align="center"
        width="230"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{ row, $index }">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            修改
          </el-button>

          <el-button size="mini" type="danger" @click="beforeDelete(row, $index)">
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

    <!-- 添加修改时弹出的框 -->
    <!-- 添加修改时弹出的框 -->
    <!-- 添加修改时弹出的框 -->
    <!-- 添加修改时弹出的框 -->
    <!-- 添加修改时弹出的框 -->
    <!-- 添加修改时弹出的框 -->
    <!-- 添加修改时弹出的框 -->
    <!-- 添加修改时弹出的框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="120px"
        style="width: 460px; margin-left: 50px"
      >
        <el-form-item label="药品名称" prop="drugname">
          <el-autocomplete
            class="inline-input"
            v-model="temp.drugname"
            :fetch-suggestions="querySearch"
            placeholder="请输入药品名称"
            @select="handleSelect"
            :debounce="0"
            clearable
          ></el-autocomplete>
        </el-form-item>

        <el-form-item label="批准文号" prop="approvalnum">
          <el-input
            v-model="temp.approvalnum"
            label="批准文号"
            placeholder="请输入批准文号"
          ></el-input>
        </el-form-item>

        <el-form-item label="生产单位" prop="company">
          <el-input
            v-model="temp.company"
            label="生产单位"
            placeholder="请输入生产单位"
          ></el-input>
        </el-form-item>

        <el-form-item label="药品本位码" prop="company">
          <el-input
            v-model="temp.standardcode"
            label="药品本位码"
            placeholder="请输入药品本位码"
          ></el-input>
        </el-form-item>

        <el-form-item label="成份" prop="ingredients">
          <el-input
            v-model="temp.ingredients"
            label="成份"
            placeholder="请输入成份"
          ></el-input>
        </el-form-item>

        <el-form-item label="分类" prop="drugclass">
          <el-input
            v-model="temp.drugclass"
            label="分类"
            placeholder="请输入分类"
          ></el-input>
        </el-form-item>

        <el-form-item label="OTC类型" prop="otc">
          <el-input
            v-model="temp.otc"
            label="OTC类型"
            placeholder="请输入OTC类型"
          ></el-input>
        </el-form-item>

        <el-form-item label="规格" prop="specifications">
          <el-input
            v-model="temp.specifications"
            label="规格"
            placeholder="请输入规格"
          ></el-input>
        </el-form-item>

        <el-form-item label="药品类型" prop="drugtype">
          <el-input
            v-model="temp.drugtype"
            label="药品类型"
            placeholder="请输入药品类型"
          ></el-input>
        </el-form-item>

        <el-form-item label="规格" prop="indication">
          <el-input
            v-model="temp.indication"
            label="规格"
            placeholder="请输入规格"
          ></el-input>
        </el-form-item>
        <el-form-item label="用法用量" prop="usageanddosage">
          <el-input
            v-model="temp.usageanddosage"
            label="用法用量"
            placeholder="请输入用法用量"
          ></el-input>
        </el-form-item>
        <el-form-item label="贮藏方式" prop="storage">
          <el-input
            v-model="temp.storage"
            label="贮藏方式"
            placeholder="请输入贮藏方式"
          ></el-input>
        </el-form-item>
        <el-form-item label="不良反应" prop="adversereaction">
          <el-input
            v-model="temp.adversereaction"
            label="不良反应"
            placeholder="请输入不良反应"
          ></el-input>
        </el-form-item>
        <el-form-item label="禁忌" prop="taboo">
          <el-input v-model="temp.taboo" label="禁忌" placeholder="请输入禁忌"></el-input>
        </el-form-item>
        <!-- <el-form-item label="禁忌" prop="taboo">
          <el-input v-model="temp.taboo" label="禁忌"></el-input>
        </el-form-item> -->
        <el-form-item label="药品监管等级" prop="drugregulatoryclassification">
          <el-input
            v-model="temp.drugregulatoryclassification"
            label="药品监管等级"
            placeholder="请输入药品监管等级"
          ></el-input>
        </el-form-item>
        <el-form-item label="功能主治" prop="functionalindications">
          <el-input
            v-model="temp.functionalindications"
            label="功能主治"
            placeholder="请输入功能主治"
          ></el-input>
        </el-form-item>
        <el-form-item label="性状" prop="character">
          <el-input
            v-model="temp.character"
            label="性状"
            placeholder="请输入性状"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="createData"> 确定 </el-button>
        <el-button @click="cancel"> 取消 </el-button>
      </div>
    </el-dialog>

    <!-- 药品详细信息弹出的框 -->
    <el-dialog title="药品详细信息" :visible.sync="dialogFormVisible2">
      <el-descriptions class="margin-top" title="" column="2" size="small" border>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-s-order"></i>
            药品名称
          </template>
          {{ this.druginfo.drugname }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-tickets"></i>
            批准文号
          </template>
          国药准字{{ this.druginfo.approvalnum }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            生产单位
          </template>
          {{ this.druginfo.company }}
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-tickets"></i>
            药品本位码
          </template>
          {{ this.druginfo.standardcode }}
          <!-- 详细信息 -->
        </el-descriptions-item>
        <el-descriptions-item v-if="this.druginfo.ingredients">
          <template slot="label">
            <i class="el-icon-coin"></i>
            成份
          </template>
          {{ this.druginfo.ingredients }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.drugclass">
          <template slot="label">
            <i class="el-icon-document-copy"></i>
            分类
          </template>
          {{ this.druginfo.drugclass }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.otc">
          <template slot="label">
            <i class="el-icon-price-tag"></i>
            OTC类型
          </template>
          {{ this.druginfo.otc }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.specifications">
          <template slot="label">
            <i class="el-icon-box"></i>
            规格
          </template>
          {{ this.druginfo.specifications }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.drugtype">
          <template slot="label">
            <i class="el-icon-files"></i>
            药品类型
          </template>
          {{ this.druginfo.drugtype }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.indication">
          <template slot="label">
            <i class="el-icon-document-checked"></i>
            适应症状
          </template>
          {{ this.druginfo.indication }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.usageanddosage">
          <template slot="label">
            <i class="el-icon-water-cup"></i>
            用法用量
          </template>
          {{ this.druginfo.usageanddosage }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.storage">
          <template slot="label">
            <i class="el-icon-takeaway-box"></i>
            贮藏方式
          </template>
          {{ this.druginfo.storage }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.adversereaction">
          <template slot="label">
            <i class="el-icon-collection-tag"></i>
            不良反应
          </template>
          {{ this.druginfo.adversereaction }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.taboo">
          <template slot="label">
            <i class="el-icon-paperclip"></i>
            禁忌
          </template>
          {{ this.druginfo.taboo }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.drugregulatoryclassification">
          <template slot="label">
            <i class="el-icon-s-data"></i>
            药品监管等级
          </template>
          {{ this.druginfo.drugregulatoryclassification }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.functionalindications">
          <template slot="label">
            <i class="el-icon-discount"></i>
            功能主治
          </template>
          {{ this.druginfo.functionalindications }}
        </el-descriptions-item>

        <el-descriptions-item v-if="this.druginfo.character">
          <template slot="label">
            <i class="el-icon-reading"></i>
            性状
          </template>
          {{ this.druginfo.character }}
        </el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script>
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination";
import {
  getdruginfo,
  getdrug,
  fetchdrugnameList,
  getsearch,
  insertdrug,
  deleteDrug,
} from "@/api/drug.js";
export default {
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        已入库: "success",
        未入库: "info",
        过期: "danger",
      };
      return statusMap[status];
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type];
    },
  },
  data() {
    return {
      drugflag: 0,
      stock: 0,
      referenceList: [],
      supplyid: "",
      tableKey: 0,
      edittime: "",
      jointime: "",
      list: null,
      total: 0,
      listLoading: false,
      druglist: [],
      listQuery: {
        standardcode: "",
        drugname: "",
        page: 1,
        limit: 10,
        sort: "+drugid",
      },
      selectOptions: [],
      selectedOptions: [],
      importanceOptions: [1, 2, 3],
      showReviewer: false,
      date: [],
      temp: {
        drugid: "",
        drugname: "",
        approvalnum: "",
        company: "",
        standardcode: "",
        ingredients: "",
        drugclass: "",
        otc: "",
        specifications: "",
        drugtype: "",
        indication: "",
        usageanddosage: "",
        storage: "",
        adversereaction: "",
        taboo: "",
        drugregulatoryclassification: "",
        functionalindications: "",
        character: "",
      },
      dialogFormVisible: false,
      dialogFormVisible2: false,
      dialogStatus: "",
      textMap: {
        update: "修改药品信息",
        create: "添加药品信息",
      },
      dialogPvVisible: false,
      pvData: [],
      options: [],
      rules: {
        drugname: [{ required: true, trigger: "blur" }],
      },
      druginfo: {
        drugid: "",
        drugname: "",
        approvalnum: "",
        company: "",
        standardcode: "",
        ingredients: "",
        drugclass: "",
        otc: "",
        specifications: "",
        drugtype: "",
        indication: "",
        usageanddosage: "",
        storage: "",
        adversereaction: "",
        taboo: "",
        drugregulatoryclassification: "",
        functionalindications: "",
        character: "",
      },
      downloadLoading: false,
    };
  },
  created() {
    this.getList();
  },
  mounted() {
    fetchdrugnameList().then((res) => (this.druglist = res.data.drugnamelist));
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
    lookDrug(row, flag) {
      getdruginfo(row.drugname).then((res) => {
        this.druginfo = res.data.druginfo;
        if (flag === true) {
          this.dialogFormVisible2 = true;
        }
      });
    },
    drugtime() {
      this.temp.exp = this.formatDate(this.temp.exp, "yyyy-MM-dd");
    },

    querySearch(queryString, cb) {
      var druglist = this.druglist;
      var results = queryString
        ? druglist.filter(this.createFilter(queryString))
        : druglist;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return (druglist) =>
        druglist.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0;
    },
    handleSelect(item) {
      let query = JSON.parse(JSON.stringify(item.value));
      this.temp.drugname = query;
      this.$refs.dataForm.validateField("drugname");
    },

    numBlur() {
      this.$refs.dataForm.validateField("quantity");
    },

    numChange(value) {
      this.temp.quantity = value;
    },

    changeList() {
      this.getList();
    },

    cancel() {
      this.dialogFormVisible = false;
      this.resetTemp;
    },
    //获取列表数据
    getList() {
      this.listLoading = false;
      if (this.listQuery.drugname != "" || this.listQuery.standardcode != "") {
        this.search();
      } else {
        getdrug(this.listQuery).then((response) => {
          this.list = response.data.allenterdrug;
          this.total = response.data.total;

          setTimeout(() => {
            this.listLoading = false;
          }, 1.5 * 1000);
        });
      }
    },

    handleFilter() {
      this.listQuery.page = 1;
      this.getList();
    },
    search() {
      //fetchSearchList
      getsearch(this.listQuery).then((response) => {
        if (response.data.total != 0) {
        } else {
          this.$notify({
            title: "提示",
            message: "未搜索到",
            type: "warning",
            duration: 2000,
          });
        }
        this.list = response.data.items;
        this.total = response.data.total;
      });
    },
    handleModifyStatus(row, status) {
      this.$message({
        message: "操作Success",
        type: "success",
      });
    },
    sortChange(data) {
      const { prop, order } = data;
      if (prop === "drugid") {
        this.sortByID(order);
      }
    },
    sortByID(order) {
      if (order === "ascending") {
        this.listQuery.sort = "+drugid";
      } else {
        this.listQuery.sort = "-drugid";
      }
      this.handleFilter();
    },
    resetTemp() {
      this.date = "";
      this.temp = {
        drugid: "",
        drugname: "",
        approvalnum: "",
        company: "",
        standardcode: "",
        ingredients: "",
        drugclass: "",
        otc: "",
        specifications: "",
        drugtype: "",
        indication: "",
        usageanddosage: "",
        storage: "",
        adversereaction: "",
        taboo: "",
        drugregulatoryclassification: "",
        functionalindications: "",
        character: "",
      };
    },

    handleCreate() {
      this.resetTemp();
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
            updateSupply(this.temp).then(() => {
              this.getList();
              this.$notify({
                title: "修改成功",
                message: "修改成功",
                type: "success",
                duration: 2000,
              });
            });
          } else if (this.dialogStatus === "create") {
            insertdrug(this.temp).then(() => {
              this.getList();
              this.$notify({
                title: "添加成功",
                message: "添加成功",
                type: "success",
                duration: 2000,
              });
            });
          }
        }
      });
    },
    handleUpdate(row) {
      // this.temp = Object.assign({}, row); // copy obj

      this.lookDrug(row, false);

      setTimeout(() => {
        this.temp = this.druginfo;
        this.dialogStatus = "update";
        this.dialogFormVisible = true;
        this.$nextTick(() => {
          this.$refs["dataForm"].clearValidate();
        });
      }, 100);
    },

    handleDelete(row, index) {
      this.list.splice(index, 1);
      let drugid = row.drugid;
      deleteDrug(drugid).then(() => {
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
    // handleDownload() {
    //   import("@/vendor/Export2Excel").then((excel) => {
    //     const tHeader = ["编号", "药品名称", "数量", "操作者ID", "取药时间", "患者名"];
    //     const filterVal = [
    //       "id",
    //       "drugname",
    //       "quantity",
    //       "handler",
    //       "jointime",
    //       "druguser",
    //     ];
    //     const listdata = this.list;
    //     const data = this.formatJson(filterVal, listdata);
    //     excel.export_json_to_excel({
    //       header: tHeader,
    //       data,
    //       filename: "取药信息-第" + this.listQuery.page + "页",
    //     });
    //   });
    // },

    // handleDownloadAll() {
    //   this.listQuery.downloadAll = true;
    //   fetchGetMedicineList(this.listQuery).then((response) => {
    //     this.listQuery.downloadAll = false;
    //     import("@/vendor/Export2Excel").then((excel) => {
    //       const tHeader = ["编号", "药品名称", "数量", "操作者ID", "取药时间", "患者名"];
    //       const filterVal = [
    //         "id",
    //         "drugname",
    //         "quantity",
    //         "handler",
    //         "jointime",
    //         "druguser",
    //       ];
    //       const listdata = response.data.allpick;
    //       const data = this.formatJson(filterVal, listdata);
    //       excel.export_json_to_excel({
    //         header: tHeader,
    //         data,
    //         filename: "取药信息(全部)",
    //       });
    //       // this.downloadLoading = true
    //     });
    //   });
    // },

    formatJson(filterVal, listdata) {
      return listdata.map((v) => filterVal.map((j) => v[j]));
    },
    getSortClass: function (key) {
      const sort = this.listQuery.sort;
      return sort === `+${key}` ? "ascending" : "descending";
    },
    formatDate(date, fmt) {
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          (date.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      }
      let o = {
        "M+": date.getMonth() + 1,
        "d+": date.getDate(),
        "h+": date.getHours(),
        "m+": date.getMinutes(),
        "s+": date.getSeconds(),
      };
      for (let k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
          let str = o[k] + "";
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length === 1 ? str : this.padLeftZero(str)
          );
        }
      }
      return fmt;
    },

    padLeftZero(str) {
      return ("00" + str).substr(str.length);
    },
  },
};
</script>
