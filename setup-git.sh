#!/bin/bash
# ═══════════════════════════════════════════════════════════════
#  setup-git.sh — Inicializa el monorepo Donaton con Git Flow
#  Uso: chmod +x setup-git.sh && ./setup-git.sh
# ═══════════════════════════════════════════════════════════════

set -e

REPO_URL="${1}"

if [ -z "$REPO_URL" ]; then
  echo "Uso: ./setup-git.sh https://github.com/TU_USUARIO/donaton.git"
  exit 1
fi

echo "🚀 Inicializando repositorio Git — Donaton Parcial 2"
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"

# ── 1. Inicializar repositorio ─────────────────────────────────
git init
git remote add origin "$REPO_URL"

# ── 2. Commit inicial en main ──────────────────────────────────
git add .
git commit -m "chore: estructura inicial del monorepo Donaton Parcial 2"
git branch -M main

# ── 3. Crear rama develop ──────────────────────────────────────
git checkout -b develop
git push -u origin main
git push -u origin develop

echo "✅ Ramas main y develop creadas"

# ── 4. Feature: MS-Donaciones Repository Pattern ───────────────
git checkout -b feature/ms-donaciones-repository
git add ms-donaciones/src/main/java/donaton/msdonaciones/repository/
git add ms-donaciones/src/main/java/donaton/msdonaciones/model/
git commit -m "feat(ms-donaciones): implementar Repository Pattern con DonacionRepository e implementación en memoria"
git checkout develop
git merge --no-ff feature/ms-donaciones-repository \
  -m "merge: integrar Repository Pattern en ms-donaciones"
git push origin develop
echo "✅ feature/ms-donaciones-repository mergeada"

# ── 5. Feature: MS-Donaciones Factory Method ───────────────────
git checkout -b feature/ms-donaciones-factory
git add ms-donaciones/src/main/java/donaton/msdonaciones/factory/
git commit -m "feat(ms-donaciones): agregar Factory Method para tipos de donación (ROPA, ALIMENTO, MEDICO, HIGIENE)"
git checkout develop
git merge --no-ff feature/ms-donaciones-factory \
  -m "merge: integrar Factory Method para tipos de donación"
git push origin develop
echo "✅ feature/ms-donaciones-factory mergeada"

# ── 6. Feature: MS-Donaciones Service + Controller + Tests ─────
git checkout -b feature/ms-donaciones-tests
git add ms-donaciones/src/main/java/donaton/msdonaciones/service/
git add ms-donaciones/src/main/java/donaton/msdonaciones/controller/
git add ms-donaciones/src/main/java/donaton/msdonaciones/MsDonacionesApplication.java
git add ms-donaciones/src/main/resources/
git add ms-donaciones/src/test/
git add ms-donaciones/pom.xml
git add ms-donaciones/README.md
git commit -m "test(ms-donaciones): agregar DonacionServiceTest y DonacionRepositoryImplTest con Mockito (15 pruebas)"
git checkout develop
git merge --no-ff feature/ms-donaciones-tests \
  -m "merge: integrar servicio, controller y pruebas unitarias de ms-donaciones"
git push origin develop
echo "✅ feature/ms-donaciones-tests mergeada"

# ── 7. Feature: MS-Logística Repository Pattern ────────────────
git checkout -b feature/ms-logistica-repository
git add ms-logistica/src/main/java/donaton/mslogistica/repository/
git add ms-logistica/src/main/java/donaton/mslogistica/model/
git commit -m "feat(ms-logistica): implementar Repository Pattern para CentroAcopio y Envio"
git checkout develop
git merge --no-ff feature/ms-logistica-repository \
  -m "merge: integrar Repository Pattern en ms-logistica"
git push origin develop
echo "✅ feature/ms-logistica-repository mergeada"

# ── 8. Feature: MS-Logística Observer Pattern ──────────────────
git checkout -b feature/ms-logistica-observer
git add ms-logistica/src/main/java/donaton/mslogistica/observer/
git commit -m "feat(ms-logistica): agregar Observer Pattern con AuditoriaEnvioObserver y NotificacionEnvioObserver"
git checkout develop
git merge --no-ff feature/ms-logistica-observer \
  -m "merge: integrar Observer Pattern para notificación de cambios de estado"
git push origin develop
echo "✅ feature/ms-logistica-observer mergeada"

# ── 9. Feature: MS-Logística Service + Controller + Tests ──────
git checkout -b feature/ms-logistica-tests
git add ms-logistica/src/main/java/donaton/mslogistica/service/
git add ms-logistica/src/main/java/donaton/mslogistica/controller/
git add ms-logistica/src/main/java/donaton/mslogistica/MsLogisticaApplication.java
git add ms-logistica/src/main/resources/
git add ms-logistica/src/test/
git add ms-logistica/pom.xml
git add ms-logistica/README.md
git commit -m "test(ms-logistica): agregar LogisticaServiceTest verificando Observer con mocks (7 pruebas)"
git checkout develop
git merge --no-ff feature/ms-logistica-tests \
  -m "merge: integrar servicio, controller y pruebas unitarias de ms-logistica"
git push origin develop
echo "✅ feature/ms-logistica-tests mergeada"

# ── 10. Feature: BFF ───────────────────────────────────────────
git checkout -b feature/bff-donaton
git add bff-donaton/
git commit -m "feat(bff): implementar Backend For Frontend que agrega MS-Donaciones y MS-Logistica en un solo endpoint"
git checkout develop
git merge --no-ff feature/bff-donaton \
  -m "merge: integrar BFF con DashboardResumenDTO y resiliencia a fallos parciales"
git push origin develop
echo "✅ feature/bff-donaton mergeada"

# ── 11. Feature: Frontend ──────────────────────────────────────
git checkout -b feature/frontend-donaton
git add frontend-donaton/
git commit -m "feat(frontend): agregar SPA React con Dashboard (BFF) y formulario de donaciones usando Facade pattern"
git checkout develop
git merge --no-ff feature/frontend-donaton \
  -m "merge: integrar frontend React con Vite"
git push origin develop
echo "✅ feature/frontend-donaton mergeada"

# ── 12. Feature: Arquetipos Maven ──────────────────────────────
git checkout -b feature/arquetipos-maven
git add arquetipos-maven/
git commit -m "feat(arquetipos): agregar arquetipo Maven para generar nuevos microservicios Donaton con estructura base"
git checkout develop
git merge --no-ff feature/arquetipos-maven \
  -m "merge: integrar arquetipo Maven donaton-microservice-archetype"
git push origin develop
echo "✅ feature/arquetipos-maven mergeada"

# ── 13. Documentación ──────────────────────────────────────────
git checkout -b feature/documentacion
git add documentacion/
git commit -m "docs: agregar PDFs de análisis de patrones, plan de branching y repositorios"
git checkout develop
git merge --no-ff feature/documentacion \
  -m "merge: integrar documentación final del Parcial 2"
git push origin develop
echo "✅ feature/documentacion mergeada"

# ── 14. Release a main ─────────────────────────────────────────
git checkout main
git merge --no-ff develop \
  -m "release: v1.0.0 Parcial 2 — BFF + 2 microservicios + 3 patrones de diseño + 27 pruebas unitarias"
git tag -a v1.0.0 -m "Parcial 2 entregado — Factory Method, Repository Pattern, Observer, BFF"
git push origin main
git push origin --tags

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo "✅ Repositorio listo en: $REPO_URL"
echo ""
echo "Ramas creadas:"
git branch -a
echo ""
echo "Historial:"
git log --oneline --graph --all | head -30
