def call() {  
  node {
    stage('Verify Workspace'){
      Map pipelineConfig = readYaml(file: "${WORKSPACE}/Jenkinsfile.yaml")
    }
    stage('Build'){
      println "Building: ${pipelineConfig.project_name}"
    }
  }
}  
