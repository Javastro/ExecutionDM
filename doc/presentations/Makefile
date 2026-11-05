.PHONY: all html sync clean build-% sync-%

# Immediate subdirectories under doc/presentations are treated as presentation projects.
SUBDIRS := $(patsubst %/,%,$(wildcard */))
SITE_PRESENTATIONS_DIR := $(abspath ../site/presentations)

all: sync

html: $(SUBDIRS:%=build-%)

sync: html $(SUBDIRS:%=sync-%)

build-%:
	$(MAKE) -C $* html

sync-%:
	@mkdir -p "$(SITE_PRESENTATIONS_DIR)/$*"
	@cp "$*/"*.html "$(SITE_PRESENTATIONS_DIR)/$*/"

clean:
	@for d in $(SUBDIRS); do \
		$(MAKE) -C $$d clean || exit $$?; \
	done


