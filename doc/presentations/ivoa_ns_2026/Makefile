PANDOC ?= pandoc
REVEALJS_VERSION := 5.2.1
MARKDOWN := ExecutionDM-Presentation.md
HTML := $(MARKDOWN:.md=.html)
PDF := $(MARKDOWN:.md=.pdf)


.PHONY: all html pdf clean

all: html pdf

html: $(HTML) | reveal.js-$(REVEALJS_VERSION)

reveal.js-$(REVEALJS_VERSION):
	curl -L https://github.com/hakimel/reveal.js/archive/refs/tags/$(REVEALJS_VERSION).tar.gz| tar xvzf -

pdf: $(PDF)

%.html: %.md
	$(PANDOC) -t revealjs \
                 --embed-resources --standalone \
                 -o ExecutionDM-Presentation.html \
                 -V revealjs-url=file:reveal.js-$(REVEALJS_VERSION)\
                 -V theme=dracula \
                 -V width=1920 -V height=1080 \
                 -V previewLinks=true \
                 --metadata title="ExecutionDM" --output $@ $<

%.pdf: %.md
	$(PANDOC) -t beamer --standalone --from markdown --pdf-engine=xelatex \
			 -V aspectratio=169 -V theme=Boadilla \
			 -V "sansfont=DejaVu Sans" -V "monofont=DejaVu Sans Mono" \
			 -V linkcolor=violet -V urlcolor=violet -V links-as-notes \
			  --output $@ $<

clean:
	rm -f $(HTML) $(PDF)

