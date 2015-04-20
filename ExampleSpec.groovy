class ExampleSpec extends MultiBrowserReportingGebSpec {
	def 'test that project permissions are updated without requiring restart'() {
    setup:
      def a = newBrowser()
      def b = newBrowser()

		given: 'archie and bob are logged in, and archie has a project'
			using a, {
				loginAs('archie', 'secret')
				createNewProject('X')
			}
      using b, {
				loginAs('bob', 'password')
				to Dashboard
				assert projectCount == 0
			}

		when: 'archie grants bob access to his project'
			using a, {
				grantProjectAccess('X', 'bob')
			}

		then: 'bob can see the project in his project list'
			b.to Dashboard
			b.projectCount == 1

		when: 'bob tries to view the project'
			b.projects[0].click()

		then: 'bob can access the project'
			b.at ProjectXInbox
	}
}
