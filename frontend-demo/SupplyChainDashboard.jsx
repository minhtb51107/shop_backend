import React, { useState, useEffect } from 'react';
import {
  Layout,
  Menu,
  Card,
  Table,
  Button,
  Modal,
  Form,
  Input,
  Select,
  DatePicker,
  Space,
  Tag,
  Statistic,
  Row,
  Col,
  Typography,
  message
} from 'antd';
import {
  DashboardOutlined,
  ShoppingOutlined,
  WarehouseOutlined,
  UserOutlined,
  SettingOutlined,
  PlusOutlined,
  EditOutlined,
  DeleteOutlined
} from '@ant-design/icons';

const { Header, Sider, Content } = Layout;
const { Title } = Typography;
const { Option } = Select;

// Mock data
const mockSuppliers = [
  { id: 1, name: 'Công ty ABC', contactPerson: 'Nguyễn Văn A', email: 'abc@email.com' },
  { id: 2, name: 'Nhà cung cấp XYZ', contactPerson: 'Trần Thị B', email: 'xyz@email.com' },
];

const mockWarehouses = [
  { id: 1, name: 'Kho Hà Nội', address: '123 Đường ABC, Hà Nội' },
  { id: 2, name: 'Kho TP.HCM', address: '456 Đường XYZ, TP.HCM' },
];

const mockPurchaseOrders = [
  {
    id: 1,
    supplierName: 'Công ty ABC',
    orderDate: '2024-01-15',
    expectedDelivery: '2024-01-20',
    status: 'PENDING',
    totalAmount: 50000000
  },
  {
    id: 2,
    supplierName: 'Nhà cung cấp XYZ',
    orderDate: '2024-01-16',
    expectedDelivery: '2024-01-22',
    status: 'APPROVED',
    totalAmount: 75000000
  },
];

const SupplyChainDashboard = () => {
  const [selectedMenu, setSelectedMenu] = useState('dashboard');
  const [suppliers, setSuppliers] = useState(mockSuppliers);
  const [warehouses, setWarehouses] = useState(mockWarehouses);
  const [purchaseOrders, setPurchaseOrders] = useState(mockPurchaseOrders);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [editingRecord, setEditingRecord] = useState(null);
  const [form] = Form.useForm();

  const menuItems = [
    { key: 'dashboard', icon: <DashboardOutlined />, label: 'Dashboard' },
    { key: 'suppliers', icon: <UserOutlined />, label: 'Nhà cung cấp' },
    { key: 'warehouses', icon: <WarehouseOutlined />, label: 'Kho hàng' },
    { key: 'purchase-orders', icon: <ShoppingOutlined />, label: 'Đơn mua hàng' },
    { key: 'settings', icon: <SettingOutlined />, label: 'Cài đặt' },
  ];

  const getStatusColor = (status) => {
    const colors = {
      'PENDING': 'orange',
      'APPROVED': 'green',
      'REJECTED': 'red',
      'DRAFT': 'blue'
    };
    return colors[status] || 'default';
  };

  const getStatusText = (status) => {
    const texts = {
      'PENDING': 'Chờ duyệt',
      'APPROVED': 'Đã duyệt',
      'REJECTED': 'Từ chối',
      'DRAFT': 'Nháp'
    };
    return texts[status] || status;
  };

  const supplierColumns = [
    {
      title: 'Tên nhà cung cấp',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Người liên hệ',
      dataIndex: 'contactPerson',
      key: 'contactPerson',
    },
    {
      title: 'Email',
      dataIndex: 'email',
      key: 'email',
    },
    {
      title: 'Thao tác',
      key: 'action',
      render: (_, record) => (
        <Space size="middle">
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>
            Sửa
          </Button>
          <Button type="link" danger icon={<DeleteOutlined />} onClick={() => handleDelete(record.id)}>
            Xóa
          </Button>
        </Space>
      ),
    },
  ];

  const warehouseColumns = [
    {
      title: 'Tên kho',
      dataIndex: 'name',
      key: 'name',
    },
    {
      title: 'Địa chỉ',
      dataIndex: 'address',
      key: 'address',
    },
    {
      title: 'Thao tác',
      key: 'action',
      render: (_, record) => (
        <Space size="middle">
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>
            Sửa
          </Button>
          <Button type="link" danger icon={<DeleteOutlined />} onClick={() => handleDelete(record.id)}>
            Xóa
          </Button>
        </Space>
      ),
    },
  ];

  const purchaseOrderColumns = [
    {
      title: 'Mã đơn',
      dataIndex: 'id',
      key: 'id',
    },
    {
      title: 'Nhà cung cấp',
      dataIndex: 'supplierName',
      key: 'supplierName',
    },
    {
      title: 'Ngày đặt',
      dataIndex: 'orderDate',
      key: 'orderDate',
    },
    {
      title: 'Ngày giao dự kiến',
      dataIndex: 'expectedDelivery',
      key: 'expectedDelivery',
    },
    {
      title: 'Trạng thái',
      dataIndex: 'status',
      key: 'status',
      render: (status) => (
        <Tag color={getStatusColor(status)}>
          {getStatusText(status)}
        </Tag>
      ),
    },
    {
      title: 'Tổng tiền',
      dataIndex: 'totalAmount',
      key: 'totalAmount',
      render: (amount) => `${amount.toLocaleString()} VNĐ`,
    },
    {
      title: 'Thao tác',
      key: 'action',
      render: (_, record) => (
        <Space size="middle">
          <Button type="link" icon={<EditOutlined />} onClick={() => handleEdit(record)}>
            Sửa
          </Button>
          <Button type="link" danger icon={<DeleteOutlined />} onClick={() => handleDelete(record.id)}>
            Xóa
          </Button>
        </Space>
      ),
    },
  ];

  const handleEdit = (record) => {
    setEditingRecord(record);
    form.setFieldsValue(record);
    setIsModalVisible(true);
  };

  const handleDelete = (id) => {
    Modal.confirm({
      title: 'Xác nhận xóa',
      content: 'Bạn có chắc chắn muốn xóa bản ghi này?',
      onOk() {
        message.success('Xóa thành công!');
      },
    });
  };

  const handleModalOk = () => {
    form.validateFields().then(values => {
      console.log('Form values:', values);
      message.success('Lưu thành công!');
      setIsModalVisible(false);
      form.resetFields();
      setEditingRecord(null);
    });
  };

  const handleModalCancel = () => {
    setIsModalVisible(false);
    form.resetFields();
    setEditingRecord(null);
  };

  const renderContent = () => {
    switch (selectedMenu) {
      case 'dashboard':
        return (
          <div>
            <Title level={2}>Dashboard Chuỗi Cung Ứng</Title>
            <Row gutter={16}>
              <Col span={6}>
                <Card>
                  <Statistic
                    title="Tổng nhà cung cấp"
                    value={suppliers.length}
                    prefix={<UserOutlined />}
                  />
                </Card>
              </Col>
              <Col span={6}>
                <Card>
                  <Statistic
                    title="Tổng kho hàng"
                    value={warehouses.length}
                    prefix={<WarehouseOutlined />}
                  />
                </Card>
              </Col>
              <Col span={6}>
                <Card>
                  <Statistic
                    title="Đơn mua hàng"
                    value={purchaseOrders.length}
                    prefix={<ShoppingOutlined />}
                  />
                </Card>
              </Col>
              <Col span={6}>
                <Card>
                  <Statistic
                    title="Tổng giá trị"
                    value={purchaseOrders.reduce((sum, order) => sum + order.totalAmount, 0)}
                    prefix="₫"
                    formatter={(value) => `${value.toLocaleString()} VNĐ`}
                  />
                </Card>
              </Col>
            </Row>
          </div>
        );

      case 'suppliers':
        return (
          <div>
            <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Title level={2}>Quản lý Nhà Cung Cấp</Title>
              <Button type="primary" icon={<PlusOutlined />} onClick={() => setIsModalVisible(true)}>
                Thêm nhà cung cấp
              </Button>
            </div>
            <Table columns={supplierColumns} dataSource={suppliers} rowKey="id" />
          </div>
        );

      case 'warehouses':
        return (
          <div>
            <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Title level={2}>Quản lý Kho Hàng</Title>
              <Button type="primary" icon={<PlusOutlined />} onClick={() => setIsModalVisible(true)}>
                Thêm kho hàng
              </Button>
            </div>
            <Table columns={warehouseColumns} dataSource={warehouses} rowKey="id" />
          </div>
        );

      case 'purchase-orders':
        return (
          <div>
            <div style={{ marginBottom: 16, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
              <Title level={2}>Quản lý Đơn Mua Hàng</Title>
              <Button type="primary" icon={<PlusOutlined />} onClick={() => setIsModalVisible(true)}>
                Tạo đơn mua hàng
              </Button>
            </div>
            <Table columns={purchaseOrderColumns} dataSource={purchaseOrders} rowKey="id" />
          </div>
        );

      default:
        return <div>Nội dung đang phát triển...</div>;
    }
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider width={250} theme="dark">
        <div style={{ height: 32, margin: 16, background: 'rgba(255, 255, 255, 0.2)', borderRadius: 6 }} />
        <Menu
          theme="dark"
          mode="inline"
          selectedKeys={[selectedMenu]}
          items={menuItems}
          onClick={({ key }) => setSelectedMenu(key)}
        />
      </Sider>
      
      <Layout>
        <Header style={{ background: '#fff', padding: '0 24px', boxShadow: '0 2px 8px rgba(0,0,0,0.1)' }}>
          <Title level={3} style={{ margin: 0, lineHeight: '64px' }}>
            Hệ Thống Quản Lý Chuỗi Cung Ứng
          </Title>
        </Header>
        
        <Content style={{ margin: '24px 16px', padding: 24, background: '#fff', borderRadius: 8 }}>
          {renderContent()}
        </Content>
      </Layout>

      <Modal
        title={editingRecord ? 'Chỉnh sửa' : 'Thêm mới'}
        open={isModalVisible}
        onOk={handleModalOk}
        onCancel={handleModalCancel}
        width={600}
      >
        <Form form={form} layout="vertical">
          {selectedMenu === 'suppliers' && (
            <>
              <Form.Item name="name" label="Tên nhà cung cấp" rules={[{ required: true }]}>
                <Input />
              </Form.Item>
              <Form.Item name="contactPerson" label="Người liên hệ">
                <Input />
              </Form.Item>
              <Form.Item name="email" label="Email" rules={[{ type: 'email' }]}>
                <Input />
              </Form.Item>
            </>
          )}
          
          {selectedMenu === 'warehouses' && (
            <>
              <Form.Item name="name" label="Tên kho" rules={[{ required: true }]}>
                <Input />
              </Form.Item>
              <Form.Item name="address" label="Địa chỉ" rules={[{ required: true }]}>
                <Input.TextArea rows={3} />
              </Form.Item>
            </>
          )}
          
          {selectedMenu === 'purchase-orders' && (
            <>
              <Form.Item name="supplierName" label="Nhà cung cấp" rules={[{ required: true }]}>
                <Select>
                  {suppliers.map(supplier => (
                    <Option key={supplier.id} value={supplier.name}>{supplier.name}</Option>
                  ))}
                </Select>
              </Form.Item>
              <Form.Item name="orderDate" label="Ngày đặt" rules={[{ required: true }]}>
                <DatePicker style={{ width: '100%' }} />
              </Form.Item>
              <Form.Item name="expectedDelivery" label="Ngày giao dự kiến">
                <DatePicker style={{ width: '100%' }} />
              </Form.Item>
              <Form.Item name="status" label="Trạng thái" rules={[{ required: true }]}>
                <Select>
                  <Option value="DRAFT">Nháp</Option>
                  <Option value="PENDING">Chờ duyệt</Option>
                  <Option value="APPROVED">Đã duyệt</Option>
                  <Option value="REJECTED">Từ chối</Option>
                </Select>
              </Form.Item>
            </>
          )}
        </Form>
      </Modal>
    </Layout>
  );
};

export default SupplyChainDashboard;
