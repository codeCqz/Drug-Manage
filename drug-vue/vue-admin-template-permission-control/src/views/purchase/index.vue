<template>
  <!-- 订单管理页面！！！ -->
  <div class="app-container">
    <div class="filter-container">
      <el-switch
        v-model="listQuery.isAll"
        inactive-text="当前用户订单"
        active-text="全部用户订单"
        @change="changeList"
        v-if="this.$store.getters.role.role != 'admin'"
      >
      </el-switch>

      <el-select
        v-model="listQuery.dealername"
        clearable
        style="width: 200px; margin-left: 10px"
        placeholder="请输入供销商名称"
        @blur="dealerBlur"
      >
        <el-option
          v-for="item in options"
          :key="item.dealerid"
          :label="item.dealername"
          :value="item.dealername"
        >
        </el-option>
      </el-select>
      <el-autocomplete
        class="inline-input"
        v-model="listQuery.drugname"
        :fetch-suggestions="querySearch"
        placeholder="请输入药品名称"
        style="width: 200px; margin-left: 10px"
        @select="handleSelect"
        clearable
      ></el-autocomplete>

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

      <div v-if="this.$store.getters.role.role != 'admin'" style="display: inline-block">
        <el-button
          type="primary"
          icon="el-icon-edit"
          style="margin-left: 10px"
          @click="handleCreate"
          >新增</el-button
        >

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
        label="订单编号"
        prop="supplyid"
        sortable="custom"
        align="center"
        width="120"
        :class-name="getSortClass('supplyid')"
      >
        <template slot-scope="{ row }">
          <span>{{ row.supplyid }}</span>
        </template>
      </el-table-column>

      <el-table-column label="供销商编号" prop="dealerid" align="center" width="100">
        <template slot-scope="{ row }">
          <span>{{ row.dealerid }}</span>
        </template>
      </el-table-column>

      <el-table-column
        label="供销商名称"
        prop="dealername"
        align="center"
        width="120"
        :class-name="getSortClass('dealerid')"
      >
        <template slot-scope="{ row }">
          <span>{{ row.dealername }}</span>
        </template>
      </el-table-column>

      <el-table-column label="药品编号" width="100px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.drugid }}</span>
        </template>
      </el-table-column>

      <el-table-column label="药品名称" width="120px" align="center">
        <template slot-scope="{ row }">
          <button
            @click="lookDrug(row)"
            style="border: none; background-color: transparent"
          >
            <span>{{ row.drugname }}</span>
          </button>
        </template>
      </el-table-column>

      <el-table-column label="数量" min-width="90px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.quantity }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作者ID" width="90px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.handler }}</span>
        </template>
      </el-table-column>
      <!-- <el-table-column label="开始时间" width="110px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.starttime }}</span>
        </template>
      </el-table-column>

      <el-table-column label="结束时间" width="110px" align="center">
        <template slot-scope="{ row }">
          <span>{{ row.endtime }}</span>
        </template>
      </el-table-column> -->

      <el-table-column label="药品状态" width="110px" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="row.status | statusFilter">
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="药品过期日期" width="110px" align="center">
        <template slot-scope="{ row }">
          {{ row.exp }}
        </template>
      </el-table-column>

      <el-table-column label="药品单价" width="110px" align="center">
        <template slot-scope="{ row }">
          {{ row.univalent }}
        </template>
      </el-table-column>

      <el-table-column label="药品总价" width="110px" align="center">
        <template slot-scope="{ row }">
          {{ row.tp }}
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
          <el-select
            v-model="temp.dealername"
            clearable
            placeholder="请选择"
            @blur="dealerBlur"
          >
            <el-option
              v-for="item in options"
              :key="item.dealerid"
              :label="item.dealername"
              :value="item.dealername"
            >
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="药品名称" prop="drugname">
          <el-autocomplete
            class="inline-input"
            v-model="temp.drugname"
            :fetch-suggestions="querySearch"
            placeholder="请输入药品名称"
            @select="handleSelect"
            clearable
          ></el-autocomplete>
        </el-form-item>
        <el-form-item label="药品数量" prop="quantity">
          <el-input-number
            v-model="temp.quantity"
            @blur="numBlur"
            @change="numChange"
            :min="1"
            :max="10000"
            label="药品数量"
          ></el-input-number>
        </el-form-item>

        <el-form-item label="药品单价" prop="univalent">
          <el-input-number
            v-model="temp.univalent"
            :precision="1"
            :step="0.1"
            :max="500"
          ></el-input-number>
        </el-form-item>

        <!-- <el-form-item label="订单起止日期" prop="date">
          <div class="block">
            <el-date-picker
              v-model="date"
              type="daterange"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd"
              @blur="dateBlur"
              @change="dateChange"
            >
            </el-date-picker>
          </div>
        </el-form-item> -->

        <!-- <el-form-item label="药品状态" prop="status">
          <el-select v-model="temp.status" placeholder="请选择" @blur="statusBlur">
            <el-option
              v-for="item in statusopt"
              :key="item.index"
              :label="item"
              :value="item"
            >
            </el-option>
          </el-select>
        </el-form-item> -->

        <v-show v-if="this.dialogStatus === `create`">
          <el-form-item label="创建日期" prop="jointime">
            <div class="block">
              <el-date-picker
                v-model="jointime"
                type="date"
                placeholder="选择日期"
                readonly
              >
              </el-date-picker>
            </div>
          </el-form-item>
        </v-show>

        <v-show v-if="this.dialogStatus != `create`">
          <el-form-item label="修改日期" prop="edittime">
            <div class="block">
              <el-date-picker
                v-model="edittime"
                type="date"
                placeholder="选择日期"
                readonly
              >
              </el-date-picker>
            </div>
          </el-form-item>
        </v-show>

        <el-form-item label="药品过期日期" prop="exp">
          <div class="block">
            <el-date-picker
              v-model="temp.exp"
              type="date"
              placeholder="选择日期"
              @blur="expBlur"
              @change="drugtime"
            >
            </el-date-picker>
          </div>
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
import {
  fetchSupplyList,
  fetchDealerInfoList,
  insertSupply,
  updateSupplyStatus,
  updateSupply,
  deleteSupply,
  getsearchsupply,
} from "@/api/supply";
import waves from "@/directive/waves"; // waves directive
import Pagination from "@/components/Pagination";
import { fetchdrugnameList, fetchbydrugname, getdruginfo } from "@/api/drug.js";

export default {
  name: "Purchase",
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        正常: "success",
        即将过期: "warning",
        过期: "danger",
      };
      return statusMap[status];
    },
    typeFilter(type) {
      return calendarTypeKeyValue[type];
    },
  },
  data() {
    const checkDealer = (rule, value, callback) => {
      if (!this.temp.dealername || this.temp.dealername.length <= 0) {
        return callback(new Error("请选择供销商！"));
      }
      return callback();
    };
    const checkDate = (rule, value, callback) => {
      if (!this.date) {
        return callback(new Error("请填入日期！"));
      } else {
        if (this.temp.status === "正常") {
          return callback();
        } else {
        }
      }
      return callback();
    };
    const checkNum = (rule, value, callback) => {
      if (this.temp.quantity > 0) {
        return callback();
      } else {
        return callback(new Error("请填入数量！"));
      }
    };

    const checkStatus = (rule, value, callback) => {
      if (!this.temp.status || this.temp.status.length <= 0) {
        return callback(new Error("请选择订单状态！"));
      } else {
        return callback();
      }
    };

    const checkDrugname = (rule, value, callback) => {
      if (!this.temp.drugname || this.temp.drugname.length <= 0) {
        return callback(new Error("请输入药品名称！"));
      } else if (this.temp.drugname || this.temp.drugname.length > 0) {
        fetchbydrugname(this.temp.drugname).then((res) => {
          if (res.data.flag === 0) {
            return callback(new Error("请输入正确的药品名称！"));
          }
        });
      }
      return callback();
    };

    const checkExp = (rule, value, callback) => {
      if (!this.temp.exp) {
        return callback(new Error("请填入日期！"));
      } else {
        // var nowDate = new Date();
        // nowDate = this.formatDate(nowDate, "yyyy-MM-dd");
        // var date = this.temp.exp;
        // if (date) {
        //   var arr = date.split("-");
        //   var arr2 = nowDate.split("-");
        //   if (arr[0] <= arr2[0]) {
        //     if (arr[1] <= arr2[1]) {
        //       if (arr[2] < arr2[2]) {
        //         return callback(new Error("请填入正确的日期！"));
        //       }
        //     } else {
        //       return callback();
        //     }
        //   }
        // }
      }
      return callback();
    };
    const checkNum2 = (rule, value, callback) => {
      if (this.temp.univalent > 0) {
        return callback();
      } else {
        return callback(new Error("请填入药品单价！"));
      }
    };

    return {
      drugflag: 0,
      statusopt: ["正常", "即将过期", "过期"],
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
        downloadAll: false,
        isAll: false,
        page: 1,
        limit: 10,
        sort: "+supplyid",
        dealername: "",
        drugname: "",
      },
      selectOptions: [],
      selectedOptions: [],
      importanceOptions: [1, 2, 3],
      showReviewer: false,
      date: [],
      temp: {
        supplyid: "0",
        dealerid: "",
        dealername: "",
        drugname: "",
        quantity: "",
        handler: "",
        status: "正常",
        isUpload: false,
        edittime: "",
        jointime: "",
        exp: "",
        univalent: "",
        TP: "",
      },
      dialogFormVisible: false,
      dialogFormVisible2: false,
      dialogStatus: "",
      textMap: {
        update: "修改进货信息",
        create: "添加进货信息",
      },
      dialogPvVisible: false,
      pvData: [],
      options: [],
      rules: {
        dealername: [{ required: true, trigger: "change", validator: checkDealer }],
        drugname: [{ required: true, trigger: "blur", validator: checkDrugname }],
        quantity: [{ required: true, trigger: "change", validator: checkNum }],
        date: [{ required: true, trigger: "change", validator: checkDate }],
        status: [{ required: true, trigger: "change", validator: checkStatus }],
        exp: [{ required: true, trigger: "change", validator: checkExp }],
        univalent: [{ required: true, trigger: "change", validator: checkNum2 }],
      },
      downloadLoading: false,
      rowedit: {
        supplyid: "",
        drugname: "",
      },
      druginfo: {
        drugid: "",
        drugname: "无",
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
    };
  },
  created() {
    if (this.$store.getters.role.role == "admin") this.listQuery.isAll = true;
    console.log(this.listQuery.isAll);
    this.getList();
    this.getoptions();
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
    lookDrug(row) {
      getdruginfo(row.drugname).then((res) => {
        this.druginfo = res.data.druginfo;
        this.dialogFormVisible2 = true;
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
      return (druglist) => {
        return druglist.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0;
      };
    },
    handleSelect(item) {
      let query = JSON.parse(JSON.stringify(item.value));
      this.temp.drugname = query;
      this.$refs.dataForm.validateField("drugname");
    },

    updateStatus(row) {
      this.rowedit.drugname = row.drugname;
      this.rowedit.supplyid = row.supplyid;
      updateSupplyStatus(this.rowedit).then(this.getList);
    },
    dateChange() {
      this.temp.starttime = this.date[0];
      this.temp.endtime = this.date[1];
    },
    numBlur() {
      this.$refs.dataForm.validateField("quantity");
    },
    statusBlur() {
      this.$refs.dataForm.validateField("status");
    },
    numChange(value) {
      this.temp.quantity = value;
    },
    dateBlur() {
      this.$refs.dataForm.validateField("date");
    },
    expBlur() {
      this.$refs.dataForm.validateField("exp");
    },
    dealerBlur() {
      this.$refs.dataForm.validateField("dealername");
    },

    getoptions() {
      fetchDealerInfoList(this.temp.drugname).then((res) => {
        this.options = res.data.dealerInfo;
      });
    },

    changeList() {
      this.getList();
    },

    cancel() {
      this.dialogFormVisible = false;
      this.resetTemp;
    },
    handleChangeSearch() {},
    handleChange() {},
    //获取列表数据
    getList() {
      this.listLoading = false;
      if (this.listQuery.drugname != "" || this.listQuery.dealername != "") {
        this.search();
      } else {
        fetchSupplyList(this.listQuery).then((response) => {
          var supplylist = JSON.parse(JSON.stringify(response.data.allSupply));

          for (let i = 0; i < supplylist.length; i++) {
            if (supplylist[i].status === 0) {
              supplylist[i].status = "正常";
            } else if (supplylist[i].status === 1) {
              supplylist[i].status = "即将过期";
            } else {
              supplylist[i].status = "过期";
            }
          }

          this.list = supplylist;
          console.log(this.list);
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
      getsearchsupply(this.listQuery).then((response) => {
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
        var supplylist = JSON.parse(JSON.stringify(this.referenceList));

        for (let i = 0; i < supplylist.length; i++) {
          if (supplylist[i].status === 0) {
            supplylist[i].status = "正常";
          } else if (supplylist[i].status === 1) {
            supplylist[i].status = "即将过期";
          } else {
            supplylist[i].status = "过期";
          }
        }
        this.list = supplylist;
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
      if (prop === "supplyid") {
        this.sortByID(order);
      }
    },
    sortByID(order) {
      if (order === "ascending") {
        this.listQuery.sort = "+supplyid";
      } else {
        this.listQuery.sort = "-supplyid";
      }
      this.handleFilter();
    },
    resetTemp() {
      this.date = "";
      this.temp = {
        supplyid: "0",
        dealerid: "",
        dealername: "",
        drugname: "",
        quantity: "",
        handler: "",
        status: "正常",
        isUpload: false,
        edittime: "",
        jointime: "",
        exp: "",
        univalent: "",
        TP: "",
      };
    },

    handleCreate() {
      this.resetTemp();
      this.dialogStatus = "create";
      var nowDate = new Date();
      nowDate = this.formatDate(nowDate, "yyyy-MM-dd");

      this.jointime = nowDate;
      this.edittime = nowDate;
      this.temp.edittime = this.edittime;
      this.temp.jointime = this.jointime;
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },
    createData() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          // this.temp.supplyid = this.list[this.list.length - 1].supplyid + 1;
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
            this.temp.handler = this.$store.getters.user;
            insertSupply(this.temp).then(() => {
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
      this.temp = Object.assign({}, row); // copy obj
      var arr = new Array();
      arr[0] = row.starttime;
      arr[1] = row.endtime;
      this.date = arr;
      var nowDate = new Date();
      nowDate = this.formatDate(nowDate, "yyyy-MM-dd");
      this.edittime = nowDate;
      this.temp.edittime = this.edittime;
      this.dialogStatus = "update";
      this.dialogFormVisible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].clearValidate();
      });
    },

    handleDelete(row, index) {
      this.list.splice(index, 1);
      this.supplyid = row.supplyid;
      deleteSupply(this.supplyid).then(() => {
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
          "订单编号",
          "供销商编号",
          "供销商名称",
          "药品编号",
          "药品名称",
          "数量",
          "操作者ID",
          "药品状态",
          "药品过期时间",
          "药品单价",
          "药品总价",
        ];
        const filterVal = [
          "supplyid",
          "dealerid",
          "dealername",
          "drugid",
          "drugname",
          "quantity",
          "handler",
          "status",
          "exp",
          "univalent",
          "tp",
        ];
        const listdata = this.list;
        const data = this.formatJson(filterVal, listdata);
        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: "进货表-第" + this.listQuery.page + "页",
        });
      });
    },

    handleDownloadAll() {
      this.listQuery.downloadAll = true;
      fetchSupplyList(this.listQuery).then((response) => {
        this.listQuery.downloadAll = false;
        import("@/vendor/Export2Excel").then((excel) => {
          const tHeader = [
            "订单编号",
            "供销商编号",
            "供销商名称",
            "药品编号",
            "药品名称",
            "数量",
            "操作者ID",
            "药品状态",
            "药品过期时间",
            "药品单价",
            "药品总价",
          ];
          const filterVal = [
            "supplyid",
            "dealerid",
            "dealername",
            "drugid",
            "drugname",
            "quantity",
            "handler",
            "status",
            "exp",
            "univalent",
            "tp",
          ];
          const listdata = response.data.allSupply;
          const data = this.formatJson(filterVal, listdata);
          excel.export_json_to_excel({
            header: tHeader,
            data,
            filename: "进货表(全部)",
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
