<template>
  <div class="login-container">
    <div class="login-box">
      <h2>登录</h2>
      <div class="form-group">
        <label for="userAccount">账号</label>
        <input 
          type="text" 
          id="userAccount" 
          v-model="userAccount" 
          placeholder="请输入账号"
          @keyup.enter="handleLogin"
        />
      </div>
      <div class="form-group">
        <label for="userPassword">密码</label>
        <input 
          type="password" 
          id="userPassword" 
          v-model="userPassword" 
          placeholder="请输入密码"
          @keyup.enter="handleLogin"
        />
      </div>
      <div class="error-message" v-if="errorMessage">{{ errorMessage }}</div>
      <div class="form-actions">
        <button class="login-btn" @click="handleLogin" :disabled="isLoading">
          {{ isLoading ? '登录中...' : '登录' }}
        </button>
        <div class="register-link">
          还没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { login as loginApi } from '../services/api';

const router = useRouter();
const route = useRoute();
const userAccount = ref('');
const userPassword = ref('');
const errorMessage = ref('');
const isLoading = ref(false);

const handleLogin = async () => {
  // 表单验证
  if (!userAccount.value || !userPassword.value) {
    errorMessage.value = '账号和密码不能为空';
    return;
  }
  
  if (userAccount.value.length < 4) {
    errorMessage.value = '账号长度不能少于4位';
    return;
  }
  
  if (userPassword.value.length < 8) {
    errorMessage.value = '密码长度不能少于8位';
    return;
  }
  
  try {
    isLoading.value = true;
    errorMessage.value = '';
    
    const data = await loginApi(userAccount.value, userPassword.value);
    if (data && data.code === 0) {
      // 登录成功，存储用户信息
      localStorage.setItem('userInfo', JSON.stringify(data.data));
      // 跳转到首页或之前的页面
      const redirect = route.query?.redirect;
      router.push(typeof redirect === 'string' && redirect ? redirect : '/models');
    } else {
      errorMessage.value = data?.message || '登录失败，请稍后再试';
    }
  } catch (error) {
    console.error('登录错误:', error);
    const serverMsg = error?.response?.data?.message;
    const status = error?.response?.status;
    const reqUrl = error?.config?.url || '';
    const isLoginReq = reqUrl.includes('/user/login');
    if (isLoginReq) {
      // 登录接口：无论 400/401/403/422 甚至 500，都更倾向提示“账号或密码错误”
      const credentialStatuses = [400, 401, 403, 404, 422, 500];
      if (credentialStatuses.includes(status)) {
        errorMessage.value = serverMsg || '账号或密码错误';
      } else if (error?.code === 'ERR_NETWORK') {
        errorMessage.value = '网络异常，请稍后再试';
      } else {
        errorMessage.value = serverMsg || error?.message || '登录失败，请稍后再试';
      }
    } else {
      // 非登录接口的兜底（当前页面不会触发到此分支，留作一致性）
      errorMessage.value = serverMsg || error?.message || '请求失败，请稍后再试';
    }
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: var(--background);
}

.login-box {
  width: 100%;
  max-width: 400px;
  padding: 30px;
  background: var(--panel);
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

h2 {
  margin-bottom: 24px;
  text-align: center;
  color: var(--text);
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 8px;
  font-size: 14px;
  color: var(--text);
}

input {
  width: 100%;
  padding: 12px;
  border: 1px solid rgba(31, 41, 55, 0.2);
  border-radius: 6px;
  background: var(--background);
  color: var(--text);
  font-size: 16px;
  transition: border-color 0.3s;
}

input:focus {
  outline: none;
  border-color: var(--accent);
}

.error-message {
  color: #ef4444;
  font-size: 14px;
  margin-bottom: 16px;
}

.form-actions {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.login-btn {
  width: 100%;
  padding: 12px;
  background: var(--accent);
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: background 0.3s;
}

.login-btn:hover {
  background: #22c55e;
}

.login-btn:disabled {
  background: #9ca3af;
  cursor: not-allowed;
}

.register-link {
  text-align: center;
  font-size: 14px;
}

.register-link a {
  color: var(--accent);
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>