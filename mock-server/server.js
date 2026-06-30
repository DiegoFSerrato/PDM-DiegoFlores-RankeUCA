const express = require('express');
const cors = require('cors');

const app = express();
const PORT = 3000;
const SUPABASE_BASE_URL = 'https://qjcxdvfzyseuvezacxsd.supabase.co/functions/v1/rankeuca';

app.use(cors());
app.use(express.json());

// Log incoming requests
app.use((req, res, next) => {
  console.log(`[${new Date().toISOString()}] ${req.method} ${req.url}`);
  next();
});

// 1. Mock Login Endpoint
app.post('/auth/login', (req, res) => {
  const { email, password } = req.body;
  
  if (email === 'serrato@gmail.com' && password === 'serrato123') {
    return res.status(200).json({
      token: 'c1452e68-ee85-4afa-ae8c-281d97fdfa54',
      user: {
        id: 1,
        name: "Diego Flores",
        email: "serrato@gmail.com"
      }
    });
  } else {
    return res.status(401).json({
      error: "Credenciales inválidas"
    });
  }
});

// 2. Proxy all other requests to Supabase
app.all('*', async (req, res) => {
  const targetUrl = `${SUPABASE_BASE_URL}${req.url}`;
  
  try {
    const headers = { ...req.headers };
    // Remove host/connection to avoid mismatch on Supabase side
    delete headers.host;
    delete headers.connection;
    
    const fetchOptions = {
      method: req.method,
      headers: headers,
    };
    
    if (['POST', 'PUT', 'PATCH'].includes(req.method) && req.body && Object.keys(req.body).length > 0) {
      fetchOptions.body = JSON.stringify(req.body);
    }
    
    const response = await fetch(targetUrl, fetchOptions);
    const contentType = response.headers.get('content-type');
    
    // Set status
    res.status(response.status);
    
    // Copy headers from proxy response
    response.headers.forEach((value, name) => {
      if (!['content-encoding', 'transfer-encoding'].includes(name.toLowerCase())) {
        res.setHeader(name, value);
      }
    });
    
    if (contentType && contentType.includes('application/json')) {
      const data = await response.json();
      res.json(data);
    } else {
      const text = await response.text();
      res.send(text);
    }
  } catch (error) {
    console.error(`Proxy Error for ${req.method} ${req.url}:`, error.message);
    res.status(500).json({ ok: false, error: 'Proxy failed to connect to Supabase' });
  }
});

app.listen(PORT, '0.0.0.0', () => {
  console.log(`Mock server is running on http://0.0.0.0:${PORT}`);
  console.log(`Supabase proxy target: ${SUPABASE_BASE_URL}`);
  console.log(`To connect from Android app, make sure your phone and PC are on the same Wi-Fi network.`);
});
