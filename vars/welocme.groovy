def call() {
  echo "${WORKSPACE}"
  
  node {
    stage('Verify Workspace'){
      Map pipelineConfig = readYaml(file: '${env.WORKSPACE}/Jenkinsfile.yaml')
    }
    stage('Build'){
      println "Building: ${pipelineConfig.project_name}"
    }
  }
}  
