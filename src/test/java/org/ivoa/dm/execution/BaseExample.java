package org.ivoa.dm.execution;


import org.ivoa.dm.pdl.*;
import org.ivoa.dm.tool.Author;
import org.ivoa.dm.tool.BuildProcess;
import org.ivoa.dm.tool.Compliance;
import org.ivoa.dm.tool.DataScaling;
import org.ivoa.dm.tool.Discovery;
import org.ivoa.dm.tool.EnvVar;
import org.ivoa.dm.tool.ImagePlatform;
import org.ivoa.dm.tool.OCIRegistry;
import org.ivoa.dm.tool.PerformanceProfile;
import org.ivoa.dm.tool.ProcessorScaling;
import org.ivoa.dm.tool.SecurityMetadata;
import org.ivoa.dm.tool.Tool;
import org.ivoa.dm.tool.ToolImage;
import org.ivoa.dm.tool.Vulnerability;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * Builds a representative {@link ExecutionModel} instance exercising most of the execution, tool and pdl models.
 */
public class BaseExample {

   private static final ZonedDateTime T0 = ZonedDateTime.of(2026, 3, 1, 9, 0, 0, 0, ZoneOffset.UTC);

   ExecutionNode jboNode;
   ExecutionNode camNode;
   Tool tool;
   ToolImage image;
   ToolImage baseImage;

   private ComputeResource compute(String name, int cores, double memory, boolean gpu, String gpuType) {
      return ComputeResource.createComputeResource(n -> {
         n.name = name;
         n.memory = memory;
         n.numberOfCores = cores;
         n.gpu = gpu;
         n.gpuType = gpuType;
      });
   }

   public ExecutionModel createModel() {
      ExecutionModel retval = new ExecutionModel();

      // ---- locations and nodes
      PhysicalLocation jbo = new PhysicalLocation("JBO", "UK", 53.2361, +2.3056);
      PhysicalLocation cam = new PhysicalLocation("Cambridge", "UK", 52.20, -0.13);
      PhysicalLocation cape = new PhysicalLocation("Cape Town", "ZA", -33.92, 18.42);

      TransferPerformance jboTransfer = new TransferPerformance(cam, 1.25e8, 0.012, 0.01, T0);

      jboNode = ExecutionNode.createExecutionNode(n -> {
         n.name = "JBO Node";
         n.location = jbo;
         n.resourcesServiceUrl = "https://jbo.example.org/execution/resources";
         n.executionServiceUrl = "https://jbo.example.org/execution/jobs";
         n.dataTransferPerformance = jboTransfer;
         n.computeResources = List.of(
               compute("Small Compute Resource", 2, 4.0, false, null),
               compute("Medium Compute Resource", 4, 8.0, false, null),
               compute("Large Compute Resource", 15, 16.0, true, "NVIDIA"));
      });

      camNode = ExecutionNode.createExecutionNode(n -> {
         n.name = "Cam Node";
         n.location = cam;
         n.resourcesServiceUrl = "https://cam.example.org/execution/resources";
         n.executionServiceUrl = "https://cam.example.org/execution/jobs";
         n.computeResources = List.of(
               compute("Small Compute Resource", 2, 4.0, false, null),
               compute("Medium Compute Resource", 4, 8.0, false, null));
      });

      // ---- data nodes, resources and workload
      DataNode jboStore = DataNode.createDataNode(d -> {
         d.storageServiceUrl = "https://jbo.example.org/storage";
         d.location = jbo;
      });
      DataNode capeStore = DataNode.createDataNode(d -> {
         d.storageServiceUrl = "s3://cape.example.org/data";
         d.location = cape;
      });
      DataResource visibilities = DataResource.createDataResource(d -> {
         d.location = jbo;
         d.size = 12.5;
         d.mimeType = "application/x-fits";
         d.accessUrl = "https://jbo.example.org/storage/obs/visibilities.fits";
      });
      DataResource results = DataResource.createDataResource(d -> {
         d.location = cape;
         d.size = 1.5;
         d.accessUrl = "s3://cape.example.org/data/results/image.fits";
      });
      WorkLoad workLoad = WorkLoad.createWorkLoad(w -> {
      });

      // ---- tool image
      Author author = Author.createAuthor(a -> {
         a.name = "Paul Harrison";
         a.email = "paul.harrison@manchester.ac.uk";
         a.github = "pahjbo";
         a.gitlab = "pharrison";
         a.orcid = "0000-0000-0000-0000";
         a.role = "maintainer";
         a.affiliation = "University of Manchester";
      });

      OCIRegistry harbor = OCIRegistry.createOCIRegistry(r -> {
         r.host = "harbor.example.org";
         r.project = "vo-tools";
      });
      OCIRegistry mirror = OCIRegistry.createOCIRegistry(r -> {
         r.host = "mirror.jbo.example.org";
         r.project = "vo-tools";
         r.proxyOf = "https://harbor.example.org";
      });

      baseImage = ToolImage.createToolImage(i -> {
         i.registries = List.of(harbor);
         i.repository = "base/python";
         i.tag = "3.12";
         i.digest = "sha256:0f1e2d3c4b5a69788796a5b4c3d2e1f00f1e2d3c4b5a69788796a5b4c3d2e1f0";
         i.build = BuildProcess.createBuildProcess(b -> b.buildCommand = "docker build .");
         i.discovery = Discovery.createDiscovery(d -> {
            d.imageTitle = "Python base image";
            d.description = "Certified Python base image";
            d.source = "https://git.example.org/base/python";
            d.version = "3.12.1";
            d.authors = List.of(author);
            d.licenses = "Apache-2.0";
            d.keyword = List.of("base-image");
            d.domain = List.of("astronomy");
            d.kind = List.of("base");
            d.software = List.of("python");
            d.isBase = true;
         });
         i.security = SecurityMetadata.createSecurityMetadata(s -> {
         });
      });

      image = ToolImage.createToolImage(i -> {
         i.registries = List.of(harbor, mirror);
         i.repository = "imager/wsclean";
         i.tag = "latest";
         i.digest = "sha256:a1b2c3d4e5f60718293a4b5c6d7e8f90a1b2c3d4e5f60718293a4b5c6d7e8f90";
         i.platforms = List.of(
               new ImagePlatform("linux", "amd64", "sha256:1111111111111111111111111111111111111111111111111111111111111111"),
               new ImagePlatform("linux", "arm64", "sha256:2222222222222222222222222222222222222222222222222222222222222222"));
         i.parentDigest = baseImage.getDigest();
         i.build = BuildProcess.createBuildProcess(b -> {
            b.buildCommand = "docker build -f docker/Dockerfile .";
            b.buildContext = "./";
            b.dockerfilePath = "docker/Dockerfile";
         });
         i.discovery = Discovery.createDiscovery(d -> {
            d.imageTitle = "Radio imager";
            d.description = "Wide-field radio interferometric imager";
            d.source = "https://git.example.org/imager";
            d.url = "https://imager.example.org";
            d.documentation = "https://imager.example.org/docs";
            d.version = "3.2.0";
            d.revision = "9f8e7d6";
            d.created = T0.minusDays(7);
            d.authors = List.of(author);
            d.licenses = "GPL-3.0-or-later";
            d.keyword = List.of("imaging", "radio", "interferometry");
            d.domain = List.of("astronomy");
            d.kind = List.of("application");
            d.software = List.of("wsclean", "casacore");
            d.isBase = false;
            d.deprecated = false;
         });
         i.security = SecurityMetadata.createSecurityMetadata(s -> {
            s.vulnerabilities = List.of(Vulnerability.createVulnerability(v -> {
               v.cve = "CVE-2025-0001";
               v.severity = "LOW";
               v.scannedAt = T0.minusDays(1);
            }));
            s.compliance = List.of(Compliance.createCompliance(c -> {
               c.signature = "MEUCIQDexample";
               c.signer = "https://harbor.example.org/signers/vo-tools";
               c.sbomUrl = "https://harbor.example.org/sbom/imager-3.2.0.json";
               c.provenanceUrl = "https://harbor.example.org/provenance/imager-3.2.0.json";
            }));
         });
      });

      // ---- tool interface
      ParameterDefinition ms = ParameterDefinition.createParameterDefinition(p -> {
         p.name = "visibilities";
         p.description = "Input measurement set";
         p.kind = new FileParameterType("application/x-fits");
         // RDB mapping requires a non-null discriminator for the polymorphic default even though it is optional
         p.defaultValue = new StringLiteral("");
         p.encoding = ValueEncoding.URI;
         p.semantics = new NullSemantic();
      });
      ParameterDefinition size = ParameterDefinition.createParameterDefinition(p -> {
         p.name = "imageSize";
         p.description = "Image size in pixels";
         p.kind = new AtomicParameterType("int");
         p.defaultValue = new IntegerLiteral(1024);
         p.semantics = new NullSemantic();
      });
      ParameterDefinition weighting = ParameterDefinition.createParameterDefinition(p -> {
         p.name = "weighting";
         p.description = "Visibility weighting scheme";
         p.kind = new AtomicParameterType("string");
         p.defaultValue = new StringLiteral("briggs");
         p.encoding = ValueEncoding.PLAIN;
         p.semantics = new NullSemantic();
      });
      ParameterDefinition verbose = ParameterDefinition.createParameterDefinition(p -> {
         p.name = "verbose";
         p.kind = new AtomicParameterType("string");
         p.defaultValue = new BooleanLiteral(false);
         p.semantics = new NullSemantic();
      });
      ParameterDefinition imageOut = ParameterDefinition.createParameterDefinition(p -> {
         p.name = "image";
         p.description = "Output image";
         p.kind = new FileParameterType("application/x-fits");
         p.semantics = new IVOASemantics("phot.flux.density", null);
         p.defaultValue = new StringLiteral("");
         p.encoding = ValueEncoding.URI;
      });

      ParameterGroup inputGroup = ParameterGroup.createParameterGroup(g -> {
         g.name = "inputs";
         g.members = List.of(
               ParameterOrGroup.createParameterOrGroup(m -> {
                  m.param = ms;
                  m.required = true;
                  m.minItems = 1;
                  m.maxItems = 1;
               }),
               ParameterOrGroup.createParameterOrGroup(m -> m.param = size),
               ParameterOrGroup.createParameterOrGroup(m -> m.param = weighting),
               ParameterOrGroup.createParameterOrGroup(m -> m.param = verbose));
      });
      ParameterGroup outputGroup = ParameterGroup.createParameterGroup(g -> {
         g.name = "outputs";
         g.members = List.of(ParameterOrGroup.createParameterOrGroup(m -> {
            m.param = imageOut;
            m.required = true;
         }));
      });

      tool = Tool.createTool(t -> {
         t.tool_id = "imager-3.2.0";
         t.version = "3.2.0";
         t.image = image;
         t.command = List.of("wsclean", "-size", "{{inputs.imageSize}}", "{{inputs.imageSize}}",
               "-weight", "{{inputs.weighting}}", "/inputs/visibilities");
         t.env = List.of(new EnvVar("OMP_NUM_THREADS", "4"), new EnvVar("TMPDIR", "/tmp"));
         t.toolInterface = ToolInterface.createToolInterface(ti -> {
            ti.parameters = List.of(ms, size, weighting, verbose, imageOut);
            ti.groups = List.of(inputGroup, outputGroup);
            ti.input = inputGroup;
            ti.output = outputGroup;
         });
         t.performanceProfile = PerformanceProfile.createPerformanceProfile(p -> {
            p.processorScaling = ProcessorScaling.createProcessorScaling(s -> {
            });
            p.dataScaling = DataScaling.createDataScaling(s -> {
            });
         });
      });

      // ---- job executions covering several lifecycle phases
      JobExecution completed = JobExecution.createJobExecution(j -> {
         j.id = "job-0001";
         j.runId = "client-run-42";
         j.ownerID = "user-123";
         j.tool = tool;
         j.resolvedImageDigest = image.getDigest();
         j.status = ExecutionPhase.COMPLETED;
         j.executionNode = jboNode;
         j.creationTime = T0;
         j.startTime = T0.plusMinutes(2);
         j.endTime = T0.plusMinutes(47);
         j.destructionTime = T0.plusDays(30);
         j.inputs = List.of(
               ParameterValue.createParameterValue(v -> {
                  v.definition = ms;
                  v.content = visibilities.getAccessUrl();
                  v.isIndirect = true;
               }),
               ParameterValue.createParameterValue(v -> {
                  v.definition = size;
                  v.content = "2048";
                  v.isIndirect = false;
               }));
         j.outputs = List.of(ParameterValue.createParameterValue(v -> {
            v.definition = imageOut;
            v.content = results.getAccessUrl();
            v.isIndirect = true;
         }));
         j.performanceMetrics = List.of(PerformanceMetric.createPerformanceMetric(m -> {
         }));
      });

      JobExecution queued = JobExecution.createJobExecution(j -> {
         j.id = "job-0002";
         j.ownerID = "user-456";
         j.tool = tool;
         j.status = ExecutionPhase.QUEUED;
         j.executionNode = camNode;
         j.creationTime = T0.plusHours(1);
         j.outputs = List.of();
         j.performanceMetrics = List.of();
         j.inputs = List.of(ParameterValue.createParameterValue(v -> {
            v.definition = ms;
            v.content = "s3://cape.example.org/data/obs2/visibilities.fits";
            v.isIndirect = true;
         }));
      });

      JobExecution failed = JobExecution.createJobExecution(j -> {
         j.id = "job-0003";
         j.ownerID = "user-123";
         j.tool = tool;
         j.status = ExecutionPhase.ERROR;
         j.executionNode = jboNode;
         j.creationTime = T0.plusHours(2);
         j.startTime = T0.plusHours(2).plusMinutes(1);
         j.endTime = T0.plusHours(2).plusMinutes(5);
         j.outputs = List.of();
         j.performanceMetrics = List.of();
         j.inputs = List.of(ParameterValue.createParameterValue(v -> {
            v.definition = verbose;
            v.content = "true";
            v.isIndirect = false;
         }));
      });

      retval.addContent(jboStore);
      retval.addContent(capeStore);
      retval.addContent(visibilities);
      retval.addContent(results);
      retval.addContent(workLoad);
      retval.addContent(completed);
      retval.addContent(queued);
      retval.addContent(failed);
      return retval;
   }
}
