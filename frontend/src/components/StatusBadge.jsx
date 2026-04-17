const STATUS_MAP = {
  PENDENTE:      { label: 'Aguard. Banco',   cls: 'badge-yellow' },
  APROVADO_BANCO:{ label: 'Aprovado Banco',  cls: 'badge-blue'   },
  REJEITADO:     { label: 'Rejeitado',       cls: 'badge-red'    },
  CANCELADO:     { label: 'Cancelado',       cls: 'badge-gray'   },
  CONCLUIDO:     { label: 'Concluído',       cls: 'badge-purple' },
};

export default function StatusBadge({ status }) {
  const { label, cls } = STATUS_MAP[status] || { label: status, cls: 'badge-gray' };
  return <span className={`badge ${cls}`}>{label}</span>;
}
