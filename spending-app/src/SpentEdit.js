import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, ButtonGroup, Container, Form, FormGroup, Input, Label } from 'reactstrap';

class SpentEdit extends Component {
  emptyItem = {
    name: '',
    description: '',
    date: '',
    amount: 0,
    tags: ''
  };

  constructor(props) {
    super(props);
    this.state = {
      item: this.emptyItem
    };
    this.handleChange = this.handleChange.bind(this);
    this.handleSubmit = this.handleSubmit.bind(this);
  }

  async componentDidMount() {
    if (this.props.match.params.id) {
      try {
        const spent = await (await fetch(`/spent/${this.props.match.params.id}`, {credentials: 'include'})).json();
        this.setState({item: spent});
      } catch (error) {
        this.props.history.push('/');
      }
    }
  }

  handleChange(event) {
    const target = event.target;
    const value = target.value;
    const name = target.name;
    let item = {...this.state.item};
    item[name] = value;
    this.setState({item});
  }

  async handleSubmit(event) {
    event.preventDefault();
    const {item} = this.state;
    item.tags = item.tags.split(",");
    await fetch('/spent' + (item.id ? '/' + item.id : '') , {
      method: (item.id) ? 'PUT' : 'POST',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(item),
      credentials: 'include'
    });
    this.props.history.push('/');
  }

  render() {
    const {item} = this.state;
    const title = <h2>{item.id ? 'Alterar Gasto' : 'Adicionar Gasto'}</h2>;

    return <div>
      <Container>
        {title}
        <Form onSubmit={this.handleSubmit}>
            <FormGroup>
                <Label for="name">Nome</Label><br/>
                <Input type="text" name="name" id="name" value={item.name || ''}
                        onChange={this.handleChange}/>
            </FormGroup>
            <FormGroup>
                <Label for="description">Descrição</Label><br/>
                <Input type="text" name="description" id="description" value={item.description || ''}
                        onChange={this.handleChange}/>
            </FormGroup>
            <FormGroup>
                <Label for="date">Data</Label><br/>
                <Input type="text" name="date" id="date" value={item.date || ''}
                        onChange={this.handleChange}/>
            </FormGroup>
            <FormGroup>
                <Label for="amount">Valor</Label><br/>
                <Input type="text" name="amount" id="amount" value={item.amount || ''}
                        onChange={this.handleChange}/>
            </FormGroup>
            <FormGroup>
                <Label for="tags">Tags</Label><br/>
                <Input type="textarea" name="tags" id="tags" value={item.tags || ''}
                        onChange={this.handleChange}/>
            </FormGroup>
            <FormGroup>
                <ButtonGroup>
                    <Button color="primary" type="submit">Salvar</Button>{' '}
                    <Button color="secondary" tag={Link} to="/">Cancelar</Button>
                </ButtonGroup>
            </FormGroup>
        </Form>
      </Container>
    </div>
  }
}

export default withRouter(SpentEdit);