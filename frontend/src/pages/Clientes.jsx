import { useEffect, useState } from 'react';
import { Plus, Pencil, Trash2, Search, Users, X, PlusCircle, MinusCircle } from 'lucide-react';
import toast from 'react-hot-toast';
import { clienteService } from '../services/api';
import ConfirmModal from '../components/ConfirmModal';

const EMPTY = { nome: '', cpf: '', rg: '', endereco: '', profissao: '', empregadores: [] };
const EMPTY_EMP = { nomeEmpresa: '', rendimento: '' };

function ClienteModal({ cliente, onClose, onSave }) {
  const editing = !!cliente?.id;
  const [form, setForm] = useState(editing
    ? { ...cliente, empregadores: (cliente.empregadores || []).map(e => ({ ...e, rendimento: String(e.rendimento) })) }
    : { ...EMPTY, empregadores: [] }
  );
  const [loading, setLoading] = useState(false);

  const set = (k) => (e) => setForm(f => ({ ...f, [k]: e.target.value }));

  const addEmp = () => {
    if (form.empregadores.length >= 3) {
      toast.error('Máximo de 3 entidades empregadoras.');
      return;
    }
    setForm(f => ({ ...f, empregadores: [...f.empregadores, { ...EMPTY_EMP }] }));
  };

  const removeEmp = (i) => setForm(f => ({
    ...f, empregadores: f.empregadores.filter((_, idx) => idx !== i)
  }));

  const setEmp = (i, k) => (e) => setForm(f => ({
    ...f,
    empregadores: f.empregadores.map((emp, idx) => idx === i ? { ...emp, [k]: e.target.value } : emp)
  }));

  const submit = async (e) => {
    e.preventDefault();
    setLoading(true);
    try {
      const payload = {
        ...form,
        empregadores: form.empregadores.map(emp => ({
          nomeEmpresa: emp.nomeEmpresa,
          rendimento: parseFloat(emp.rendimento),
        })),
      };
      if (editing) {
        await clienteService.atualizar(cliente.id, payload);
        toast.success('Cliente atualizado!');
      } else {
        await clienteService.criar(payload);
        toast.success('Cliente cadastrado!');
      }
      onSave();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao salvar cliente.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div className="modal modal-lg" onClick={e => e.stopPropagation()}>
        <div className="modal-header">
          <h2>{editing ? 'Editar Cliente' : 'Novo Cliente'}</h2>
          <button className="btn-icon" onClick={onClose}><X size={16} /></button>
        </div>
        <div className="modal-body">
          <form onSubmit={submit}>
            <div className="form-grid">
              <div className="form-group span-2">
                <label>Nome completo *</label>
                <input className="form-control" value={form.nome} onChange={set('nome')} placeholder="Ex: João da Silva" required />
              </div>
              <div className="form-group">
                <label>CPF *</label>
                <input className="form-control" value={form.cpf} onChange={set('cpf')} placeholder="000.000.000-00" required />
              </div>
              <div className="form-group">
                <label>RG *</label>
                <input className="form-control" value={form.rg} onChange={set('rg')} placeholder="MG-00.000.000" required />
              </div>
              <div className="form-group span-2">
                <label>Endereço *</label>
                <input className="form-control" value={form.endereco} onChange={set('endereco')} placeholder="Rua, número, bairro, cidade/UF" required />
              </div>
              <div className="form-group span-2">
                <label>Profissão *</label>
                <input className="form-control" value={form.profissao} onChange={set('profissao')} placeholder="Ex: Engenheiro de Software" required />
              </div>
            </div>

            {/* Empregadores */}
            <div style={{ marginTop: '1rem', marginBottom: '.5rem' }}>
              <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '.5rem' }}>
                <span style={{ fontWeight: 600, fontSize: '.85rem', color: 'var(--gray-700)' }}>
                  Entidades Empregadoras ({form.empregadores.length}/3)
                </span>
                {form.empregadores.length < 3 && (
                  <button type="button" className="btn btn-ghost btn-sm" onClick={addEmp}>
                    <PlusCircle size={14} /> Adicionar
                  </button>
                )}
              </div>
              {form.empregadores.length === 0 && (
                <p style={{ fontSize: '.8rem', color: 'var(--gray-400)', fontStyle: 'italic' }}>
                  Nenhuma entidade empregadora cadastrada (opcional).
                </p>
              )}
              {form.empregadores.map((emp, i) => (
                <div key={i} style={{ display: 'grid', gridTemplateColumns: '1fr 1fr auto', gap: '.5rem', marginBottom: '.5rem', alignItems: 'end' }}>
                  <div className="form-group" style={{ marginBottom: 0 }}>
                    {i === 0 && <label style={{ fontSize: '.75rem' }}>Nome da empresa *</label>}
                    <input className="form-control" required value={emp.nomeEmpresa}
                      onChange={setEmp(i, 'nomeEmpresa')} placeholder="Ex: TechCorp Ltda" />
                  </div>
                  <div className="form-group" style={{ marginBottom: 0 }}>
                    {i === 0 && <label style={{ fontSize: '.75rem' }}>Rendimento (R$) *</label>}
                    <input className="form-control" required type="number" min="0.01" step="0.01"
                      value={emp.rendimento} onChange={setEmp(i, 'rendimento')} placeholder="5000.00" />
                  </div>
                  <button type="button" className="btn-icon danger" onClick={() => removeEmp(i)} title="Remover">
                    <MinusCircle size={16} />
                  </button>
                </div>
              ))}
            </div>

            <div className="form-actions">
              <button type="button" className="btn btn-ghost" onClick={onClose}>Cancelar</button>
              <button type="submit" className="btn btn-primary" disabled={loading}>
                {loading ? 'Salvando...' : editing ? 'Salvar Alterações' : 'Cadastrar Cliente'}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default function Clientes() {
  const [clientes, setClientes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState('');
  const [modal, setModal] = useState(null);
  const [deleting, setDeleting] = useState(null);
  const [delLoading, setDelLoading] = useState(false);

  const load = () => {
    setLoading(true);
    clienteService.listar()
      .then(r => setClientes(r.data.items ?? r.data))
      .catch(() => toast.error('Erro ao carregar clientes.'))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  const filtered = clientes.filter(c =>
    c.nome.toLowerCase().includes(search.toLowerCase()) ||
    c.cpf.includes(search)
  );

  const handleDelete = async () => {
    setDelLoading(true);
    try {
      await clienteService.deletar(deleting.id);
      toast.success('Cliente removido.');
      setDeleting(null);
      load();
    } catch (err) {
      toast.error(err.response?.data?.message || 'Erro ao remover.');
    } finally {
      setDelLoading(false);
    }
  };

  const fmtRenda = (emps) => {
    if (!emps || emps.length === 0) return '—';
    const total = emps.reduce((s, e) => s + (e.rendimento || 0), 0);
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(total);
  };

  return (
    <div className="page">
      <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', marginBottom: '1.5rem' }}>
        <div>
          <h2 style={{ fontSize: '1.25rem', fontWeight: 700, color: 'var(--gray-900)' }}>Clientes</h2>
          <p style={{ color: 'var(--gray-500)', fontSize: '.875rem' }}>Gerencie os clientes cadastrados</p>
        </div>
        <button className="btn btn-primary" onClick={() => setModal('create')}>
          <Plus size={16} /> Novo Cliente
        </button>
      </div>

      <div className="panel">
        <div className="panel-header">
          <span className="panel-title">{filtered.length} cliente{filtered.length !== 1 ? 's' : ''}</span>
          <div className="search-bar">
            <Search size={15} className="search-icon" />
            <input
              className="form-control"
              placeholder="Buscar por nome ou CPF..."
              value={search}
              onChange={e => setSearch(e.target.value)}
            />
          </div>
        </div>

        {loading ? (
          <div className="loading-center"><div className="spinner" /></div>
        ) : filtered.length === 0 ? (
          <div className="empty-state">
            <Users size={36} />
            <p>{search ? 'Nenhum cliente encontrado para esta busca.' : 'Nenhum cliente cadastrado ainda.'}</p>
          </div>
        ) : (
          <div className="table-wrap">
            <table>
              <thead>
                <tr>
                  <th>#</th>
                  <th>Nome</th>
                  <th>CPF</th>
                  <th>Profissão</th>
                  <th>Empregadores</th>
                  <th>Renda Total</th>
                  <th>Ações</th>
                </tr>
              </thead>
              <tbody>
                {filtered.map(c => (
                  <tr key={c.id}>
                    <td style={{ color: 'var(--gray-400)', fontSize: '.8rem' }}>{c.id}</td>
                    <td style={{ fontWeight: 500 }}>{c.nome}</td>
                    <td>{c.cpf}</td>
                    <td>{c.profissao}</td>
                    <td>
                      <span className="badge badge-neutral">{c.empregadores?.length || 0}</span>
                    </td>
                    <td>{fmtRenda(c.empregadores)}</td>
                    <td>
                      <div className="td-actions">
                        <button className="btn-icon" title="Editar" onClick={() => setModal(c)}>
                          <Pencil size={15} />
                        </button>
                        <button className="btn-icon danger" title="Remover" onClick={() => setDeleting(c)}>
                          <Trash2 size={15} />
                        </button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>

      {modal && (
        <ClienteModal
          cliente={modal === 'create' ? null : modal}
          onClose={() => setModal(null)}
          onSave={() => { setModal(null); load(); }}
        />
      )}

      {deleting && (
        <ConfirmModal
          title="Remover Cliente"
          message={`Tem certeza que deseja remover o cliente "${deleting.nome}"? Esta ação não pode ser desfeita.`}
          onConfirm={handleDelete}
          onCancel={() => setDeleting(null)}
          loading={delLoading}
        />
      )}
    </div>
  );
}
