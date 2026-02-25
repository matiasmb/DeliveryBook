#!/bin/bash
# Añade la clave SSH pública a GitHub (vía API) y hace push.
# Uso: export GITHUB_TOKEN=ghp_xxx; ./scripts/setup-github-push.sh
# Crear token en: https://github.com/settings/tokens (scope: admin:public_key)

set -e
KEY_FILE="$HOME/.ssh/id_ed25519.pub"
REPO_DIR="$(cd "$(dirname "$0")/.." && pwd)"

if [[ -z "$GITHUB_TOKEN" ]]; then
  echo "No está definido GITHUB_TOKEN."
  echo ""
  echo "Para que el script añada tu clave SSH a GitHub y haga push:"
  echo "  1. Creá un token: https://github.com/settings/tokens (scope: admin:public_key)"
  echo "  2. Ejecutá: export GITHUB_TOKEN=ghp_tu_token"
  echo "  3. Volvé a ejecutar: $0"
  echo ""
  echo "Tu clave pública (también podés añadirla a mano en https://github.com/settings/keys):"
  cat "$KEY_FILE"
  exit 1
fi

if [[ ! -f "$KEY_FILE" ]]; then
  echo "No se encuentra $KEY_FILE"
  exit 1
fi

TITLE="DeliveryBook-$(hostname)-$(date +%Y%m%d)"
KEY_CONTENT=$(cat "$KEY_FILE")
KEY_JSON=$(echo "$KEY_CONTENT" | python3 -c "import sys,json; print(json.dumps(sys.stdin.read().strip()))" 2>/dev/null || echo "\"$(echo "$KEY_CONTENT" | sed 's/"/\\"/g')\"")

echo "Añadiendo clave SSH a GitHub..."
curl -s -X POST -H "Authorization: token $GITHUB_TOKEN" \
  -H "Accept: application/vnd.github.v3+json" \
  https://api.github.com/user/keys \
  -d "{\"title\":\"$TITLE\",\"key\":$KEY_JSON}" > /dev/null || true

# Ignorar si ya existía; probar push
cd "$REPO_DIR"
echo "Haciendo push a origin main..."
git push -u origin main
