export function useAuth() {
  try {
    return JSON.parse(localStorage.getItem('auth_user')) || null;
  } catch {
    return null;
  }
}

export const ROLES = {
  ADMIN:   'admin',
  AGENTE:  'agente',
  CLIENTE: 'cliente',
};

export function hasRole(user, ...roles) {
  return user && roles.includes(user.role);
}
