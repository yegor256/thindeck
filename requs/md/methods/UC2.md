The following table describes all technical details of Use Case `UC2`:

<table>
      <thead>
         <tr>
            <th>Property</th>
            <th>Details</th>
         </tr>
      </thead>
      <tbody>
         <tr>
            <td>ID</td>
            <td>UC2</td>
         </tr>
         <tr>
            <td>Signature</td>
            <td>
               <code>_self</code> registers</td>
         </tr>
         <tr>
            <td>Primary</td>
            <td>User</td>
         </tr>
         <tr>
            <td>Actors</td>
            <td/>
         </tr>
         <tr>
            <td>Brief</td>
            <td>"The user enters a new unique name of the repository, its URI and an optional SSH key (should be all in one form). Then he clicks ADD and the repository appears in the list of other repositories in his account"</td>
         </tr>
      </tbody>
   </table>

Actors taking participation in the Use Case have the following properties:

<table>
      <thead>
         <tr>
            <th>Actor</th>
            <th>Properties</th>
         </tr>
      </thead>
      <tbody>
         <tr>
            <td>User</td>
            <td>"a person identified in the system (logged in)"<ul>
                  <li>
                     <code>repo</code>: Repository</li>
                  <li>
                     <code>URN</code>:  "a unique identifier of itself, for example urn:github:526301"</li>
                  <li>
                     <code>authTokens</code>:  "a list of auth tokens to each auth provider the user connected to, e.g., Goole, Facebook, etc."</li>
                  <li>
                     <code>balance</code>:  "amount of money available, can be stored as integer number of cents"</li>
               </ul>
            </td>
         </tr>
      </tbody>
   </table>
