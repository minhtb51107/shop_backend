# Hướng Dẫn Setup Frontend Cho Hệ Thống Quản Lý Chuỗi Cung Ứng

## 🚀 Cài Đặt Nhanh

### Bước 1: Tạo Project React
```bash
# Tạo project mới
npx create-react-app shop-frontend
cd shop-frontend

# Cài đặt các dependencies cần thiết
npm install antd @ant-design/icons axios react-router-dom
```

### Bước 2: Cấu hình Proxy
Thêm vào `package.json`:
```json
{
  "proxy": "http://localhost:8080"
}
```

### Bước 3: Tạo API Service
```javascript
// src/services/api.js
import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor để thêm token
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
```

### Bước 4: Tạo Services cho Module 2
```javascript
// src/services/supplyChainService.js
import api from './api';

export const supplierService = {
  getAll: () => api.get('/suppliers'),
  getById: (id) => api.get(`/suppliers/${id}`),
  create: (data) => api.post('/suppliers', data),
  update: (id, data) => api.put(`/suppliers/${id}`, data),
  delete: (id) => api.delete(`/suppliers/${id}`),
};

export const warehouseService = {
  getAll: () => api.get('/api/v1/warehouses'),
  getById: (id) => api.get(`/api/v1/warehouses/${id}`),
  create: (data) => api.post('/api/v1/warehouses', data),
  update: (id, data) => api.put(`/api/v1/warehouses/${id}`, data),
  delete: (id) => api.delete(`/api/v1/warehouses/${id}`),
};

export const purchaseOrderService = {
  getAll: () => api.get('/api/v1/purchase-orders'),
  getById: (id) => api.get(`/api/v1/purchase-orders/${id}`),
  create: (data) => api.post('/api/v1/purchase-orders', data),
  update: (id, data) => api.put(`/api/v1/purchase-orders/${id}`, data),
  delete: (id) => api.delete(`/api/v1/purchase-orders/${id}`),
};
```

## 🎨 Các UI Framework Khác

### Option 1: Next.js + Tailwind CSS
```bash
npx create-next-app@latest shop-frontend --typescript --tailwind --eslint
cd shop-frontend
npm install @headlessui/react @heroicons/react
```

### Option 2: Vue.js + Vuetify
```bash
npm create vue@latest shop-frontend
cd shop-frontend
npm install vuetify @mdi/font
```

### Option 3: Angular + Angular Material
```bash
ng new shop-frontend --routing --style=scss
cd shop-frontend
ng add @angular/material
```

## 📱 Responsive Design

### Mobile-First Approach
```css
/* src/styles/responsive.css */
@media (max-width: 768px) {
  .ant-layout-sider {
    position: fixed !important;
    height: 100vh;
    z-index: 1000;
  }
  
  .ant-layout-content {
    margin-left: 0 !important;
  }
}
```

## 🔐 Authentication Integration

### Login Component
```javascript
// src/components/Login.jsx
import { Form, Input, Button, Card } from 'antd';
import { UserOutlined, LockOutlined } from '@ant-design/icons';
import { useNavigate } from 'react-router-dom';

const Login = () => {
  const navigate = useNavigate();
  
  const onFinish = async (values) => {
    try {
      const response = await api.post('/api/v1/auth/login', values);
      localStorage.setItem('token', response.data.token);
      navigate('/dashboard');
    } catch (error) {
      message.error('Đăng nhập thất bại');
    }
  };

  return (
    <Card title="Đăng Nhập" style={{ width: 400, margin: '100px auto' }}>
      <Form onFinish={onFinish}>
        <Form.Item name="email" rules={[{ required: true }]}>
          <Input prefix={<UserOutlined />} placeholder="Email" />
        </Form.Item>
        <Form.Item name="password" rules={[{ required: true }]}>
          <Input.Password prefix={<LockOutlined />} placeholder="Mật khẩu" />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" block>
            Đăng Nhập
          </Button>
        </Form.Item>
      </Form>
    </Card>
  );
};
```

## 🚀 Deployment

### Build cho Production
```bash
npm run build
```

### Deploy lên Vercel/Netlify
```bash
# Vercel
npm install -g vercel
vercel

# Netlify
npm install -g netlify-cli
netlify deploy --prod --dir=build
```

## 📊 Charts & Analytics

### Cài đặt Chart.js
```bash
npm install chart.js react-chartjs-2
```

### Dashboard với Charts
```javascript
import { Line, Bar, Pie } from 'react-chartjs-2';

const DashboardCharts = () => {
  const data = {
    labels: ['Tháng 1', 'Tháng 2', 'Tháng 3', 'Tháng 4'],
    datasets: [{
      label: 'Doanh thu',
      data: [12000000, 19000000, 3000000, 5000000],
      borderColor: 'rgb(75, 192, 192)',
      tension: 0.1
    }]
  };

  return (
    <Row gutter={16}>
      <Col span={12}>
        <Card title="Biểu đồ doanh thu">
          <Line data={data} />
        </Card>
      </Col>
      <Col span={12}>
        <Card title="Thống kê tồn kho">
          <Bar data={data} />
        </Card>
      </Col>
    </Row>
  );
};
```

## 🎯 Best Practices

1. **Component Structure**: Tách components nhỏ, dễ maintain
2. **State Management**: Sử dụng Redux Toolkit hoặc Zustand
3. **Error Handling**: Implement error boundaries
4. **Loading States**: Sử dụng Ant Design Spin component
5. **Form Validation**: Sử dụng Ant Design Form validation
6. **Responsive**: Mobile-first design approach
7. **Performance**: Lazy loading, code splitting
8. **Security**: Input sanitization, XSS protection

## 🔧 Development Tools

```bash
# ESLint + Prettier
npm install --save-dev eslint prettier eslint-config-prettier

# Storybook cho component development
npx storybook@latest init

# Testing
npm install --save-dev @testing-library/react @testing-library/jest-dom
```
