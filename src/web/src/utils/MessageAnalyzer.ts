export class MessageAnalyzer {
  private regExp = new RegExp('@(\\d+)', 'g')
  private messageWithDependencies: string

  constructor(messageWithDependencies: string) {
    this.messageWithDependencies = messageWithDependencies
  }

  public extractMessage(): string {
    return this.messageWithDependencies.replace(this.regExp, '')
  }

  public extractDependencies(): number[] {
    let execReg = this.regExp.exec(this.messageWithDependencies)
    const dependencies = Array<number>()
    while (execReg != null) {
      dependencies.push(parseInt(execReg[1], 10))
      execReg = this.regExp.exec(this.messageWithDependencies)
    }
    return dependencies
  }
}
