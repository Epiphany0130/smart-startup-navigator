<template>
  <div class="register-container">
    <div class="register-box">
      <h2>注册</h2>
      <div class="form-group">
        <label for="userAccount">账号</label>
        <input 
          type="text" 
          id="userAccount" 
          v-model="userAccount" 
          placeholder="请输入账号（至少4位）"
        />
      </div>
      <div class="form-group">
        <label for="userPassword">密码</label>
        <input 
          type="password" 
          id="userPassword" 
          v-model="userPassword" 
          placeholder="请输入密码（至少8位）"
        />
      </div>
      <div class="form-group">
        <label for="checkPassword">确认密码</label>
        <input 
          type="password" 
          id="checkPassword" 
          v-model="checkPassword" 
          placeholder="请再次输入密码"
        />
      </div>
      <div class="error-message" v-if="errorMessage">{{ errorMessage }}</div>
      <div class="form-actions">
        <button class="register-btn" @click="handleRegister" :disabled="isLoading">
          {{ isLoading ? '注册中...' : '注册' }}
        </button>
        <div class="login-link">
          已有账号？<router-link to="/login">立即登录</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { api } from '../services/api';

const router = useRouter();
const userAccount = ref('');
const userPassword = ref('');
const checkPassword = ref('');
const errorMessage = ref('');
const isLoading = ref(false);

const handleRegister = async () => {
  // 表单验证
  if (!userAccount.value || !userPassword.value || !checkPassword.value) {
    errorMessage.value = '所有字段都不能为空';
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
  
  if (userPassword.value !== checkPassword.value) {
    errorMessage.value = '两次输入的密码不一致';
    return;
  }
  
  try {
    isLoading.value = true;
    errorMessage.value = '';
    
    const response = await api.post('/user/register', {
      userAccount: userAccount.value,
      userPassword: userPassword.value,
      checkPassword: checkPassword.value
    });
    
    if (response.data && response.data.code === 0) {
      // 注册成功，跳转到登录页
      router.push('/login');
    } else {
      errorMessage.value = response.data?.message || '注册失败，请稍后再试';
    }
  } catch (error) {
    console.error('注册错误:', error);
    errorMessage.value = error.response?.data?.message || '注册失败，请检查网络连接';
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: var(--background);
}

.register-box {
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

.register-btn {
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

.register-btn:hover {
  background: #22c55e;
}

.register-btn:disabled {
  background: #9ca3af;
  cursor: not-allowed;
}

.login-link {
  text-align: center;
  font-size: 14px;
}

.login-link a {
  color: var(--accent);
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>