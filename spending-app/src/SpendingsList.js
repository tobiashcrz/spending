import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import { Link, withRouter } from 'react-router-dom';

class SpendingsList extends Component {
  state = {
    isLoading: true,
    spendings: []
  };

  async componentDidMount() {
    const response = await fetch('/spendings');
    const body = await response.json();
    this.setState({ spendings: body, isLoading: false });
  }

  render() {
    const {spendings, isLoading} = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    const spendingList = spendings.map(spent => {
      return <tr key={spent.id}>
        <td style={{textAlign: 'left'}}>{spent.name}</td>
        <td style={{textAlign: 'left'}}>{spent.description}</td>
        <td style={{textAlign: 'left'}}>{spent.date}</td>
        <td style={{textAlign: 'left'}}>{spent.amount}</td>
        <td style={{textAlign: 'left'}}>{spent.tags}</td>
        <td>
            <ButtonGroup>
                <Button size="sm" color="primary" tag={Link} to={"/spent/" + spent.id}>Alterar</Button>
            </ButtonGroup>
        </td>
      </tr>
    });

    return (
      <div>
        <Container fluid>
          <div className="float-right">
            <Button color="success" tag={Link} to="/spent">Adicionar Gasto</Button>
          </div>
          <h3>Lista de Gastos</h3>
          <Table className="table">
            <thead>
            <tr className="tr_header">
              <th style={{textAlign: 'left'}}>Name</th>
              <th style={{textAlign: 'left'}}>Descrição</th>
              <th style={{textAlign: 'left'}}>Data</th>
              <th style={{textAlign: 'left'}}>Valor</th>
              <th style={{textAlign: 'left'}}>Tags</th>
              <th style={{textAlign: 'left', width: '10%'}}></th>
            </tr>
            </thead>
            <tbody>
            {spendingList}
            </tbody>
          </Table>
        </Container>
      </div>
    );
  }
}

export default withRouter(SpendingsList);