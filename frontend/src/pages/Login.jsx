import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Car, Lock, User } from 'lucide-react';
import { authService } from '../services/api';

export default function Login({ onLogin }) {
  const navigate = useNavigate();
  const [usuario, setUsuario] = useState('');
  const [senha, setSenha] = useState('');
  const [erro, setErro] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setErro('');
    try {
      const { data } = await authService.login({ usuario, senha });
      const userObj = {
        usuario:  data.usuario,
        nome:     data.nome,
        role:     data.role,
        clienteId: data.clienteId ?? null,
        agenteId:  data.agenteId  ?? null,
      };
      localStorage.setItem('auth_token', data.token);
      localStorage.setItem('auth_user', JSON.stringify(userObj));
      onLogin(userObj);
      if (data.role === 'admin' || data.role === 'agente') navigate('/dashboard');
      else if (data.role === 'banco')   navigate('/banco/pedidos');
      else if (data.role === 'empresa') navigate('/empresa/pedidos');
      else navigate('/');
    } catch (err) {
      setErro(err.response?.status === 401
        ? 'Usuário ou senha incorretos.'
        : 'Erro ao conectar com o servidor.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={{
      minHeight: 'calc(100vh - 60px)', display: 'flex', alignItems: 'center',
      justifyContent: 'center', padding: '2rem 1rem',
      background: 'linear-gradient(160deg, #f0f9ff 0%, #e0f2fe 50%, #f8fafc 100%)',
    }}>
      <div style={{
        background: '#fff', borderRadius: 16, padding: '2.5rem 2rem',
        width: '100%', maxWidth: 380, boxShadow: '0 20px 50px rgba(0,0,0,.12)',
      }}>
        {/* Logo */}
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <div style={{
            width: 50, height: 50, borderRadius: 12,
            background: 'linear-gradient(135deg, #2563eb, #1d4ed8)',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            margin: '0 auto .75rem', boxShadow: '0 6px 18px rgba(37,99,235,.3)',
          }}>
            <Car size={24} color="#fff" />
          </div>
          <h1 style={{ fontSize: '1.25rem', fontWeight: 800, color: '#111827' }}>Entrar na conta</h1>
          <p style={{ fontSize: '.82rem', color: '#6b7280', marginTop: '.2rem' }}>
            Novo por aqui?{' '}
            <Link to="/cadastro" style={{ color: '#2563eb', fontWeight: 600 }}>Criar conta grátis</Link>
          </p>
        </div>

        <form onSubmit={handleSubmit}>
          {/* Usuário */}
          <div style={{ marginBottom: '1rem' }}>
            <label style={{ display: 'block', fontSize: '.78rem', fontWeight: 600, color: '#374151', marginBottom: '.35rem' }}>
              Usuário
            </label>
            <div style={{ position: 'relative' }}>
              <User size={15} style={{ position: 'absolute', left: '.7rem', top: '50%', transform: 'translateY(-50%)', color: '#9ca3af' }} />
              <input style={inputStyle} value={usuario}
                onChange={e => { setUsuario(e.target.value); setErro(''); }}
                placeholder="seu.usuario" required autoComplete="username"
                onFocus={e => e.target.style.borderColor = '#2563eb'}
                onBlur={e => e.target.style.borderColor = '#e5e7eb'} />
            </div>
          </div>

          {/* Senha */}
          <div style={{ marginBottom: '1.5rem' }}>
            <label style={{ display: 'block', fontSize: '.78rem', fontWeight: 600, color: '#374151', marginBottom: '.35rem' }}>
              Senha
            </label>
            <div style={{ position: 'relative' }}>
              <Lock size={15} style={{ position: 'absolute', left: '.7rem', top: '50%', transform: 'translateY(-50%)', color: '#9ca3af' }} />
              <input type="password" style={inputStyle} value={senha}
                onChange={e => { setSenha(e.target.value); setErro(''); }}
                placeholder="••••••••" required autoComplete="current-password"
                onFocus={e => e.target.style.borderColor = '#2563eb'}
                onBlur={e => e.target.style.borderColor = '#e5e7eb'} />
            </div>
          </div>

          {erro && (
            <div style={{
              background: '#fef2f2', border: '1px solid #fecaca', color: '#dc2626',
              borderRadius: 8, padding: '.55rem .85rem', fontSize: '.82rem',
              marginBottom: '1rem', fontWeight: 500,
            }}>{erro}</div>
          )}

          <button type="submit" disabled={loading} style={{
            width: '100%', padding: '.65rem', borderRadius: 8, border: 'none',
            background: loading ? '#93c5fd' : '#2563eb', color: '#fff',
            fontWeight: 700, fontSize: '.9rem', cursor: loading ? 'not-allowed' : 'pointer',
            fontFamily: 'inherit', transition: 'background .15s',
          }}>
            {loading ? 'Entrando...' : 'Entrar'}
          </button>
        </form>

        {/* Dica */}
        <div style={{
          marginTop: '1.5rem', background: '#f0f9ff', borderRadius: 8,
          padding: '.7rem .85rem', border: '1px solid #bae6fd',
        }}>
          <p style={{ fontSize: '.7rem', fontWeight: 700, color: '#0369a1', marginBottom: '.3rem', textTransform: 'uppercase', letterSpacing: '.05em' }}>
            Contas de demonstração
          </p>
          {[['admin','admin123'],['banco1','banco123'],['empresa1','empresa123'],['cliente','12345']].map(([u, s]) => (
            <p key={u} style={{ fontSize: '.75rem', color: '#0c4a6e', fontFamily: 'monospace' }}>
              {u} / {s}
            </p>
          ))}
        </div>
      </div>
    </div>
  );
}

const inputStyle = {
  width: '100%', padding: '.5rem .75rem .5rem 2.15rem',
  border: '1.5px solid #e5e7eb', borderRadius: 7, fontSize: '.875rem',
  color: '#111827', outline: 'none', fontFamily: 'inherit',
  transition: 'border-color .15s',
};
