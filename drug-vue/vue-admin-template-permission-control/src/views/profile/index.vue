<template>
  <div class="app-container">
    <div v-if="user">
      <el-row :gutter="20">
        <el-col :span="6" :xs="24">
          <user-card :user="comuser" />
        </el-col>

        <el-col :span="18" :xs="24">
          <el-card>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="个人信息" name="account">
                <account :user="comuser" />
              </el-tab-pane>

              <el-tab-pane label="上传头像" name="activity">
                <activity />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import UserCard from "./components/UserCard";
import Activity from "./components/Activity";
import Account from "./components/Account";

export default {
  name: "Profile",
  components: { UserCard, Account, Activity },
  data() {
    return {
      comuser: {},
      activeTab: "account",
    };
  },
  computed: {
    ...mapGetters(["name", "avatar", "roles", "user"]),
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser() {
      this.comuser = this.user;
    },
  },
};
</script>
