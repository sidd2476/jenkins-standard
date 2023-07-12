def call() {  
  node {
    stage('Verify Workspace'){
      echo "${env.GIT_LOCAL_BRANCH}"
      Map pipelineConfig = readYaml(file: "${WORKSPACE}/Jenkinsfile.yaml")
    }
    stage('Build'){
      println "Building: ${pipelineConfig.project_name}"
    }
  }
}  
