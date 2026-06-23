.PHONY: brand-init brand-validate brand-apply brand-icons brand-splash brand-dry-run

brand-init:
	./scripts/brand init

brand-validate:
	./scripts/brand validate

brand-apply:
	./scripts/brand apply all

brand-icons:
	./scripts/brand apply icons

brand-splash:
	./scripts/brand apply splash

brand-dry-run:
	./scripts/brand apply all --dry-run
