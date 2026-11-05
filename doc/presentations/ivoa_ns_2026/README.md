# ExecutionDM Presentation

This directory contains a pandoc-compatible presentation in Markdown format covering the ExcutionDM data model.

## Files

- **`ExecutionDM-Presentation.md`** — Main presentation source (pandoc-compatible Markdown)
- **`README.md`** — This file

## How to Generate Slides

### Install Pandoc and Reveal.js

```bash
# macOS
brew install pandoc
```

# For HTML5 slides with Reveal.js

This should be done automatically by the Makefile, but if you want to do it manually, you can download and extract Reveal.js:

```
curl -L https://github.com/hakimel/reveal.js/archive/refs/tags/5.2.1.tar.gz| tar xvzf -
```

### Generate HTML5 Slides (Reveal.js)

```bash
make html
  
```

Then open `ExecutionDM-Presentation.html` in a web browser.

### Generate PDF Slides (via Beamer)

```bash
make pdf
```

Requires LaTeX/XeTeX to be installed:
```bash
brew install basictex  # macOS minimal LaTeX
tlmgr install beamer   # Install Beamer package
```
and requires Dejavu fonts to be installed for PDF output:

```bash
 brew install --cask font-dejavu
```

### Generate PowerPoint Slides

```bash
pandoc ExecutionDM-Presentation.md \
  -o ExecutionDM-Presentation.pptx
```

##  Documentation
- [Pandoc User Guide](https://pandoc.org/MANUAL.html#slide-shows)
- [Reveal.js Documentation](https://revealjs.com/)





