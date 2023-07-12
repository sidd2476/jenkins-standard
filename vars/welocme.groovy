def call() {
  echo "${env.WORKSPACE}"
  Map pipelineConfig = readYaml(file: "${env.WORKSPACE}/Jenkinsfile.yaml")
  node {
    stage('Build'){
      println "Building: ${pipelineConfig.project_name}"
    }
  }
}  
