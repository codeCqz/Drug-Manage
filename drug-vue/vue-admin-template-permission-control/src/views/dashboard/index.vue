<template>
  <div class="dashboard-container">
    <component :is="currentRole" />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import adminDashboard from "./admin";
import pickerDashboard from "./picker";
export default {
  name: "Dashboard",
  components: { adminDashboard, editorDashboard, pickerDashboard },
  data() {
    return {
      currentRole: "editorDashboard",
    };
  },
  computed: {
    ...mapGetters(["roles"]),
  },
};
</script>
