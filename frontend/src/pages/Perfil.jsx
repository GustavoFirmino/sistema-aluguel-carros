import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { agenteService, clienteService } from '../services/api';
import toast from 'react-hot-toast';
import { Landmark, DollarSign, CheckCircle, AlertCircle } from 'lucide-react';

export default function Perfil({ usuario }) {
  const navigate  = useNavigate();
  const clienteId = usuario?.clienteId;
  const [cliente, setCliente]   = useState(null);
  const [bancos,  setBancos]    = useState([]);
  const [form,    setForm]      = useState({ rendaMensal: '', bancoAgenteId: '' });
  const [saving,  setSaving]    = useState(false);

  useEffect(() => {
    if (!clienteId) return;
    clienteService.buscarPorId(clienteId).then(r => {
      setCliente(r.data);
      setForm({
        rendaMensal:  r.data.rendaMensal ?? '',
        bancoAgenteId: r.data.bancoAgenteId ?? '',
      });
    }).catch(() => toast.error('Erro ao carregar perfil.'));

    agenteService.listar('BANCO').then(r => setBancos(r.data));
  }, [clienteId]);

  const salvar = async (e) => {
    e.preventDefault();
    if (!form.bancoAgenteId) { toast.error('Selecione um banco.'); return; }
    setSaving(true);
    try {
      await clienteService.atualizar(clienteId, {
        nome: cliente.nome,
        cpf: cliente.cpf,
        rg: cliente.rg,
        endereco: cliente.endereco,
        profissao: cliente.profissao,
        empregadores: cliente.empregadores ?? [],
        rendaMensal: parseFloat(form.rendaMensal) || null,
        bancoAgenteId: parseInt(form.bancoAgenteId),
      });
      toast.success('Perfil atualizado! Agora você pode alugar um veículo.');
      navigate('/');
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao salvar.');
    } finally {
      setSaving(false);
    }
  };

  if (!clienteId) return (
    <div style={{ maxWidth: 480, margin: '4rem auto', padding: '2rem', textAlign: 'center' }}>
      <p>Conta não vinculada a um perfil de cliente. Contacte o administrador.</p>
    </div>
  );

  return (
    <div style={{ maxWidth: 560, margin: '3rem auto', padding: '0 1.5rem' }}>

      {/* Banner de perfil incompleto */}
      {cliente && !cliente.bancoAgenteId && (
        <div style={{ background: '#fff7ed', border: '1px solid #fed7aa', borderRadius: 12, padding: '1rem 1.25rem', marginBottom: '1.25rem', display: 'flex', gap: '.75rem', alignItems: 'flex-start' }}>
          <AlertCircle size={20} color="#f97316" style={{ flexShrink: 0, marginTop: '.1rem' }} />
          <div>
            <div style={{ fontWeight: 700, fontSize: '.92rem', color: '#c2410c', marginBottom: '.2rem' }}>
              Perfil incompleto — Passo 2 de 4
            </div>
            <div style={{ fontSize: '.82rem', color: '#9a3412', lineHeight: 1.6 }}>
              Informe sua renda mensal e selecione seu banco parceiro para poder criar pedidos de aluguel.
            </div>
          </div>
        </div>
      )}

      <div style={{ background: '#fff', borderRadius: 16, border: '1px solid #e5e7eb', boxShadow: '0 4px 24px rgba(0,0,0,.07)', padding: '2rem' }}>
        <div style={{ marginBottom: '1.75rem', textAlign: 'center' }}>
          <div style={{ width: 56, height: 56, borderRadius: '50%', background: 'linear-gradient(135deg,#3b82f6,#1d4ed8)', display: 'flex', alignItems: 'center', justifyContent: 'center', margin: '0 auto .75rem' }}>
            <CheckCircle size={28} color="#fff" />
          </div>
          <h2 style={{ fontSize: '1.25rem', fontWeight: 800, color: '#111827' }}>Complete seu Perfil</h2>
          <p style={{ color: '#6b7280', fontSize: '.875rem', marginTop: '.25rem' }}>
            Informe sua renda e selecione seu banco para poder alugar veículos.
          </p>
        </div>

        <form onSubmit={salvar} style={{ display: 'flex', flexDirection: 'column', gap: '1.25rem' }}>
          <div>
            <label style={{ display: 'block', fontSize: '.8rem', fontWeight: 600, color: '#374151', marginBottom: '.4rem' }}>
              <DollarSign size={13} style={{ marginRight: 4, verticalAlign: 'middle' }} />
              Renda Mensal (R$)
            </label>
            <input
              className="form-control"
              type="number" min="0" step="0.01" placeholder="Ex: 5000.00"
              value={form.rendaMensal}
              onChange={e => setForm(f => ({ ...f, rendaMensal: e.target.value }))}
            />
          </div>

          <div>
            <label style={{ display: 'block', fontSize: '.8rem', fontWeight: 600, color: '#374151', marginBottom: '.4rem' }}>
              <Landmark size={13} style={{ marginRight: 4, verticalAlign: 'middle' }} />
              Banco onde recebe seu salário *
            </label>
            <select
              className="form-control" required
              value={form.bancoAgenteId}
              onChange={e => setForm(f => ({ ...f, bancoAgenteId: e.target.value }))}
            >
              <option value="">Selecione seu banco...</option>
              {bancos.map(b => (
                <option key={b.id} value={b.id}>{b.nome}</option>
              ))}
            </select>
            <p style={{ fontSize: '.75rem', color: '#9ca3af', marginTop: '.3rem' }}>
              O banco selecionado realizará a análise de crédito do seu pedido.
            </p>
          </div>

          <button type="submit" className="btn btn-primary" disabled={saving} style={{ width: '100%', justifyContent: 'center', marginTop: '.5rem' }}>
            {saving ? 'Salvando...' : 'Salvar e Começar a Alugar'}
          </button>
        </form>
      </div>
    </div>
  );
}
