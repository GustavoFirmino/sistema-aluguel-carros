import { useState } from 'react';
import { Car, Lock, User } from 'lucide-react';

const USUARIOS = [
  { usuario: 'admin',   senha: 'admin123',  nome: 'Administrador' },
  { usuario: 'agente',  senha: 'agente123', nome: 'Agente Locadora' },
  { usuario: 'cliente', senha: '12345',     nome: 'Operador Clientes' },
];

export default function Login({ onLogin }) {
  const [usuario, setUsuario] = useState('');
  const [senha, setSenha] = useState('');
  const [erro, setErro] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = (e) => {
    e.preventDefault();
    setLoading(true);
    setErro('');

    setTimeout(() => {
      const encontrado = USUARIOS.find(u => u.usuario === usuario && u.senha === senha);
      if (encontrado) {
        localStorage.setItem('auth_user', JSON.stringify(encontrado));
        onLogin(encontrado);
      } else {
        setErro('Usuário ou senha incorretos.');
      }
      setLoading(false);
    }, 600);
  };

  return (
    <div style={{
      minHeight: '100vh', display: 'flex', alignItems: 'center', justifyContent: 'center',
      background: 'linear-gradient(135deg, #1e3a8a 0%, #2563eb 50%, #3b82f6 100%)',
      padding: '1rem',
    }}>
      <div style={{
        background: '#fff', borderRadius: 16, padding: '2.5rem 2rem',
        width: '100%', maxWidth: 400, boxShadow: '0 25px 50px rgba(0,0,0,.25)',
      }}>
        {/* Logo */}
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <div style={{
            width: 56, height: 56, borderRadius: 14,
            background: 'linear-gradient(135deg, #2563eb, #1d4ed8)',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            margin: '0 auto 1rem',
            boxShadow: '0 8px 20px rgba(37,99,235,.35)',
          }}>
            <Car size={28} color="#fff" />
          </div>
          <h1 style={{ fontSize: '1.35rem', fontWeight: 800, color: '#111827' }}>
            Aluguel de Carros
          </h1>
          <p style={{ fontSize: '.85rem', color: '#6b7280', marginTop: '.3rem' }}>
            Sistema de Gestão — PUC Minas
          </p>
        </div>

        {/* Formulário */}
        <form onSubmit={handleSubmit}>
          <div style={{ marginBottom: '1rem' }}>
            <label style={{ display: 'block', fontSize: '.8rem', fontWeight: 600, color: '#374151', marginBottom: '.4rem' }}>
              Usuário
            </label>
            <div style={{ position: 'relative' }}>
              <User size={16} style={{ position: 'absolute', left: '.75rem', top: '50%', transform: 'translateY(-50%)', color: '#9ca3af' }} />
              <input
                style={{
                  width: '100%', padding: '.6rem .75rem .6rem 2.25rem',
                  border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: '.875rem',
                  color: '#111827', outline: 'none', transition: 'border-color .15s',
                }}
                value={usuario}
                onChange={e => { setUsuario(e.target.value); setErro(''); }}
                placeholder="admin"
                required
                autoComplete="username"
                onFocus={e => e.target.style.borderColor = '#2563eb'}
                onBlur={e => e.target.style.borderColor = '#d1d5db'}
              />
            </div>
          </div>

          <div style={{ marginBottom: '1.5rem' }}>
            <label style={{ display: 'block', fontSize: '.8rem', fontWeight: 600, color: '#374151', marginBottom: '.4rem' }}>
              Senha
            </label>
            <div style={{ position: 'relative' }}>
              <Lock size={16} style={{ position: 'absolute', left: '.75rem', top: '50%', transform: 'translateY(-50%)', color: '#9ca3af' }} />
              <input
                type="password"
                style={{
                  width: '100%', padding: '.6rem .75rem .6rem 2.25rem',
                  border: '1.5px solid #d1d5db', borderRadius: 8, fontSize: '.875rem',
                  color: '#111827', outline: 'none', transition: 'border-color .15s',
                }}
                value={senha}
                onChange={e => { setSenha(e.target.value); setErro(''); }}
                placeholder="••••••••"
                required
                autoComplete="current-password"
                onFocus={e => e.target.style.borderColor = '#2563eb'}
                onBlur={e => e.target.style.borderColor = '#d1d5db'}
              />
            </div>
          </div>

          {erro && (
            <div style={{
              background: '#fef2f2', border: '1px solid #fecaca', color: '#dc2626',
              borderRadius: 8, padding: '.6rem .85rem', fontSize: '.82rem',
              marginBottom: '1rem', fontWeight: 500,
            }}>
              {erro}
            </div>
          )}

          <button
            type="submit"
            disabled={loading}
            style={{
              width: '100%', padding: '.7rem', borderRadius: 8,
              background: loading ? '#93c5fd' : '#2563eb',
              color: '#fff', fontWeight: 700, fontSize: '.9rem',
              border: 'none', cursor: loading ? 'not-allowed' : 'pointer',
              transition: 'background .15s',
            }}
          >
            {loading ? 'Entrando...' : 'Entrar'}
          </button>
        </form>

        {/* Dica de credenciais */}
        <div style={{
          marginTop: '1.5rem', background: '#f0f9ff', borderRadius: 8,
          padding: '.75rem', border: '1px solid #bae6fd',
        }}>
          <p style={{ fontSize: '.72rem', fontWeight: 700, color: '#0369a1', marginBottom: '.4rem', textTransform: 'uppercase', letterSpacing: '.05em' }}>
            Credenciais de demonstração
          </p>
          {USUARIOS.map(u => (
            <p key={u.usuario} style={{ fontSize: '.78rem', color: '#0c4a6e', fontFamily: 'monospace' }}>
              {u.usuario} / {u.senha}
            </p>
          ))}
        </div>
      </div>
    </div>
  );
}
