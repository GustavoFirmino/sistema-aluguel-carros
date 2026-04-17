import { useState } from 'react';
import { Link } from 'react-router-dom';
import { Car, User, Eye, EyeOff } from 'lucide-react';
import { authService } from '../services/api';

export default function Cadastro({ onLogin }) {
  const [form, setForm] = useState({
    usuario: '', senha: '', confirmarSenha: '',
    nome: '', cpf: '', rg: '', endereco: '', profissao: '',
  });
  const [showSenha, setShowSenha] = useState(false);
  const [erro, setErro] = useState('');
  const [loading, setLoading] = useState(false);

  const set = (k) => (e) => { setForm(f => ({ ...f, [k]: e.target.value })); setErro(''); };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (form.senha !== form.confirmarSenha) { setErro('As senhas não coincidem.'); return; }
    if (form.senha.length < 4) { setErro('A senha deve ter ao menos 4 caracteres.'); return; }

    setLoading(true);
    try {
      const { data } = await authService.cadastro({
        usuario: form.usuario, senha: form.senha,
        nome: form.nome, cpf: form.cpf, rg: form.rg,
        endereco: form.endereco, profissao: form.profissao,
      });
      const userObj = { usuario: data.usuario, nome: data.nome, role: data.role, clienteId: data.clienteId ?? null, agenteId: null };
      localStorage.setItem('auth_token', data.token);
      localStorage.setItem('auth_user', JSON.stringify(userObj));
      onLogin(userObj);
      // O redirect para /perfil é feito pelo guard da rota /cadastro em App.jsx
    } catch (err) {
      setErro(err.response?.data?.message || 'Erro ao realizar cadastro. Tente novamente.');
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
        background: '#fff', borderRadius: 16, padding: '2rem',
        width: '100%', maxWidth: 520, boxShadow: '0 20px 50px rgba(0,0,0,.12)',
      }}>
        {/* Header */}
        <div style={{ textAlign: 'center', marginBottom: '1.75rem' }}>
          <div style={{
            width: 48, height: 48, borderRadius: 12,
            background: 'linear-gradient(135deg, #2563eb, #1d4ed8)',
            display: 'flex', alignItems: 'center', justifyContent: 'center',
            margin: '0 auto .75rem',
          }}>
            <Car size={24} color="#fff" />
          </div>
          <h1 style={{ fontSize: '1.25rem', fontWeight: 800, color: '#111827' }}>Criar conta</h1>
          <p style={{ fontSize: '.82rem', color: '#6b7280', marginTop: '.2rem' }}>
            Já tem conta? <Link to="/login" style={{ color: '#2563eb', fontWeight: 600 }}>Entrar</Link>
          </p>
        </div>

        <form onSubmit={handleSubmit}>
          <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '.85rem' }}>

            {/* Nome */}
            <div style={{ gridColumn: 'span 2' }}>
              <Field label="Nome completo *" type="text" value={form.nome} onChange={set('nome')}
                placeholder="João da Silva" required icon={<User size={15} />} />
            </div>

            {/* Usuário */}
            <div style={{ gridColumn: 'span 2' }}>
              <Field label="Nome de usuário *" type="text" value={form.usuario} onChange={set('usuario')}
                placeholder="joaosilva" required />
            </div>

            {/* Senha */}
            <Field label="Senha *" type={showSenha ? 'text' : 'password'} value={form.senha}
              onChange={set('senha')} placeholder="Mínimo 4 caracteres" required
              suffix={
                <button type="button" onClick={() => setShowSenha(s => !s)}
                  style={{ background: 'none', border: 'none', cursor: 'pointer', color: '#9ca3af', display: 'flex' }}>
                  {showSenha ? <EyeOff size={15} /> : <Eye size={15} />}
                </button>
              } />

            {/* Confirmar Senha */}
            <Field label="Confirmar senha *" type={showSenha ? 'text' : 'password'}
              value={form.confirmarSenha} onChange={set('confirmarSenha')} placeholder="Repita a senha" required />

            {/* CPF */}
            <Field label="CPF *" type="text" value={form.cpf} onChange={set('cpf')}
              placeholder="000.000.000-00" required />

            {/* RG */}
            <Field label="RG *" type="text" value={form.rg} onChange={set('rg')}
              placeholder="MG-00.000.000" required />

            {/* Endereço */}
            <div style={{ gridColumn: 'span 2' }}>
              <Field label="Endereço *" type="text" value={form.endereco} onChange={set('endereco')}
                placeholder="Rua, número, bairro, cidade/UF" required />
            </div>

            {/* Profissão */}
            <div style={{ gridColumn: 'span 2' }}>
              <Field label="Profissão *" type="text" value={form.profissao} onChange={set('profissao')}
                placeholder="Ex: Engenheiro de Software" required />
            </div>
          </div>

          {erro && (
            <div style={{
              background: '#fef2f2', border: '1px solid #fecaca', color: '#dc2626',
              borderRadius: 8, padding: '.55rem .85rem', fontSize: '.82rem',
              margin: '.75rem 0', fontWeight: 500,
            }}>{erro}</div>
          )}

          <button
            type="submit" disabled={loading}
            style={{
              width: '100%', padding: '.65rem', borderRadius: 8, border: 'none',
              background: loading ? '#93c5fd' : '#2563eb', color: '#fff',
              fontWeight: 700, fontSize: '.9rem', cursor: loading ? 'not-allowed' : 'pointer',
              marginTop: '.75rem', fontFamily: 'inherit', transition: 'background .15s',
            }}>
            {loading ? 'Criando conta...' : 'Criar conta e entrar'}
          </button>
        </form>
      </div>
    </div>
  );
}

function Field({ label, suffix, icon, ...props }) {
  return (
    <div>
      <label style={{ display: 'block', fontSize: '.77rem', fontWeight: 600, color: '#374151', marginBottom: '.3rem' }}>
        {label}
      </label>
      <div style={{ position: 'relative', display: 'flex', alignItems: 'center' }}>
        {icon && <span style={{ position: 'absolute', left: '.65rem', color: '#9ca3af', display: 'flex' }}>{icon}</span>}
        <input
          {...props}
          style={{
            width: '100%', padding: `.48rem .75rem .48rem ${icon ? '2.1rem' : '.75rem'}`,
            border: '1.5px solid #e5e7eb', borderRadius: 7, fontSize: '.875rem',
            color: '#111827', outline: 'none', fontFamily: 'inherit',
            paddingRight: suffix ? '2.2rem' : '.75rem',
          }}
          onFocus={e => e.target.style.borderColor = '#2563eb'}
          onBlur={e => e.target.style.borderColor = '#e5e7eb'}
        />
        {suffix && <span style={{ position: 'absolute', right: '.6rem', display: 'flex' }}>{suffix}</span>}
      </div>
    </div>
  );
}
