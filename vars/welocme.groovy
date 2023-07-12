def call() {
  Map pipelineConfig = readYaml(file: "${WORKSPACE}/Jenkinsfile.yaml }")
  node {
    stage('Build'){
      println "Building: ${pipelineConfig.project_name}"
    }
  }
}  
